package com.teamzero.member.service;

import static com.teamzero.member.exception.ErrorCode.MEMBER_EMAIL_AUTH_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_SIGNUP_EMAIL_DUPLICATE;
import static com.teamzero.member.exception.ErrorCode.MEMBER_SIGNUP_SEND_AUTH_EMAIL_FAIL;
import static com.teamzero.member.exception.ErrorCode.TOKEN_NOT_VALID;

import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.Modify;
import com.teamzero.member.domain.model.dto.SignUp;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.TeamZeroException;
import java.util.Optional;
import java.util.UUID;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final JavaMailSender javaMailSender;

    public MemberEntity findByMemberIdAndEmail(Long memberId, String email) {
        return memberRepository.findByMemberIdAndEmail(memberId, email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
    }

    /**
     * 회원가입시 아이디 중복 체크
     * 중복데이터가 있으면 에러발생
     * 중복데이터가 없으면 email을 반환
     */
    public String findByEmail(String email) {
        if (memberRepository.countByEmail(email) > 0) {
            throw new TeamZeroException(MEMBER_SIGNUP_EMAIL_DUPLICATE);
        }
        return email;
    }

    /**
     * 회원가입
     * 회원가입 요청 할 때 필수값 체크와 비밀번호확인은 프론트에서 한다고 판단.
     * 초기값 셋팅
     * memberGrade 부분 수정필요
     * 회원가입 후 메일 전송
     */
    @Transactional
    public MemberEntity memberRegister(SignUp request) {

        String encPassword = this.createEncPassword(request.getPassword());

        String emailAuthKey = UUID.randomUUID().toString();
        var member = MemberEntity.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(encPassword)
                .memberStatus(MemberStatus.NO_AUTH)
                .emailAuthKey(emailAuthKey)
                .emailAuthYn(false)
            //구독여부 초기값 추가하였습니다.
                .subscribeYn(false)
                .build();

        memberRepository.save(member);
        if (!this.memberSignUpSendEmailAuth(request.getEmail(), emailAuthKey)) {
            throw new TeamZeroException(MEMBER_SIGNUP_SEND_AUTH_EMAIL_FAIL);
        }
        return member;
    }

    /**
     * 회원가입 후 이메일 인증
     * 1. 아이디와 인증키로 정상데이터 체크
     * 2. 정상 데이터 맞으면 인증여부 true로 변경
     */
    public String memberEmailAuthCheck(String email, String emailAuthKey) {
        var countByEmailAndEmailAuthKey = memberRepository.countByEmailAndEmailAuthKey(email, emailAuthKey);

        if (countByEmailAndEmailAuthKey <= 0) {
            throw new TeamZeroException(MEMBER_EMAIL_AUTH_NOT_FOUND);
        }

        var member = memberRepository.findByEmail(email).orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
        member.setEmailAuthYn(true);
        memberRepository.save(member);

        return "Email Authentication Success";
    }

    /**
     * 회원가입시 gmail을 이용한 인증 이메일 전송
     * 제목과 본문은 수정필요
     */
    private boolean memberSignUpSendEmailAuth(String email, String emailAuthKey) {
        boolean result = false;
        String subject = "제로몰 인증 이메일 입니다.";
        String text = "http://localhost:8081/member/email-auth?key=" + emailAuthKey + "&id=" + email;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);
            }
        };
        try {
            javaMailSender.send(msg);
            result = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * 비밀번호 암호화 함수 (Encrypt)
     */
    private String createEncPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public MemberEntity modifyMember(Modify member) {
        if (!isMemberExist(member.getMemberId())) {
            throw new TeamZeroException(MEMBER_NOT_FOUND);
        }
        MemberEntity memberEntity = memberRepository.findById(member.getMemberId()).get();

        String encPassword = this.createEncPassword(member.getPassword());
        memberEntity.setNickname(member.getNickname());
        memberEntity.setPassword(encPassword);

        return memberEntity;
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public boolean withdrawMember(String email) {

        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new TeamZeroException(MEMBER_NOT_FOUND);
        }

        MemberEntity memberEntity = memberRepository.findByEmail(email).get();

        memberEntity.setMemberStatus(MemberStatus.WITHDRAW);

        memberRepository.save(memberEntity);

        return true;
    }


    /**
     * 회원 존재 여부 조회
     * - 매개변수 : memberId
     */
    public boolean isMemberExist(Long memberId) {
        Optional<MemberEntity> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isPresent()) {
            return true;
        }
        return false;
    }

}
