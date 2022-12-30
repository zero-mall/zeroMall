package com.teamzero.member.application;

import static com.teamzero.member.exception.ErrorCode.MEMBER_EMAIL_AUTH_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_SIGNUP_EMAIL_DUPLICATE;
import static com.teamzero.member.exception.ErrorCode.MEMBER_SIGNUP_SEND_AUTH_EMAIL_FAIL;
import static com.teamzero.member.util.UserApiUtils.createEncPassword;

import com.teamzero.member.domain.model.AdminEntity;
import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.constants.AdminStatus;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.dto.AdminInfoDto;
import com.teamzero.member.domain.model.dto.SignUpDto;
import com.teamzero.member.domain.repository.AdminRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.TeamZeroException;
import java.util.UUID;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

  private final MemberRepository memberRepository;
  private final AdminRepository adminRepository;
  private final JavaMailSender javaMailSender;
  /**
   * 회원가입
   * 회원가입 요청 할 때 필수값 체크와 비밀번호확인은 프론트에서 한다고 판단.
   * 초기값 셋팅
   * memberGrade 부분 수정필요
   * 회원가입 후 메일 전송
   */
  public SignUpDto.Response register(SignUpDto.Request request) {

    String encPassword = createEncPassword(request.getPassword());

    String emailAuthKey = UUID.randomUUID().toString();
    MemberEntity member = MemberEntity.builder()
        .email(request.getEmail())
        .nickname(request.getNickname())
        .age(request.getAge())
        .password(encPassword)
        .memberStatus(MemberStatus.NO_AUTH)
        .emailAuthKey(emailAuthKey)
        .emailAuthYn(false)
        //구독여부 초기값 추가하였습니다.
        .subscribeYn(false)
        .build();

    memberRepository.save(member);
    if (memberSignUpSendEmailAuth(request.getEmail(), emailAuthKey)) {
      throw new TeamZeroException(MEMBER_SIGNUP_SEND_AUTH_EMAIL_FAIL);
    }
    return SignUpDto.Response.builder()
        .email(member.getEmail())
        .age(member.getAge())
        .nickname(member.getNickname())
        .build();
  }

  /**
   * 회원가입시 아이디 중복 체크
   * 중복데이터가 있으면 에러발생
   * 중복데이터가 없으면 true반환
   */
  public boolean exsitsByEmail(String email) {
    if(memberRepository.exsitsByEmail(email)){
      throw new TeamZeroException(MEMBER_SIGNUP_EMAIL_DUPLICATE);
    }
    return true;
  }

  /**
   * 회원가입 후 이메일 인증
   * 1. 아이디와 인증키로 정상데이터 체크
   * 2. 정상 데이터 맞으면 인증여부 true로 변경
   */
  public SignUpDto.Response memberEmailAuthCheck(String email, String emailAuthKey) {
    MemberEntity member = memberRepository.findByEmail(email).orElseThrow(()
        -> new TeamZeroException(MEMBER_NOT_FOUND));

    if (!memberRepository.exsitsByEmailAndEmailAuthKey(email, emailAuthKey)) {
      throw new TeamZeroException(MEMBER_EMAIL_AUTH_NOT_FOUND);
    }

    member.setEmailAuthYn(true);
    memberRepository.save(member);

    return SignUpDto.Response.builder()
        .email(member.getEmail())
        .age(member.getAge())
        .nickname(member.getNickname())
        .build();
  }


  /**
   * Admin 회원가입
   * AdminStatus 는 계정 생성시 IN_USE를 default로
   * 가입시 해당 이메일 존재하는경우 실패 응답
   */
  public AdminInfoDto registerAdmin(SignUpDto.Request singUp) {
    if (!adminRepository.existsByEmail(singUp.getEmail())) {
      throw new TeamZeroException(MEMBER_SIGNUP_EMAIL_DUPLICATE);
    }
    String encPassword = createEncPassword(singUp.getPassword());
    AdminEntity admin = AdminEntity.builder()
        .email(singUp.getEmail())
        .password(encPassword)
        .adminStatus(AdminStatus.IN_USE)
        .build();

    adminRepository.save(admin);
    return AdminInfoDto.fromEntity(admin);
  }

  /**
   * 회원가입시 gmail을 이용한 인증 이메일 전송
   * 제목과 본문은 수정필요
   */
  @Value("${email.auth.send.url}")
  private String emailSendUrl;
  private boolean memberSignUpSendEmailAuth(String email, String emailAuthKey) {

    boolean result = false;
    String subject = "제로몰 인증 이메일 입니다.";
    String authUrl = String.format(emailSendUrl, email, emailAuthKey);
    String content = "링크를 클릭하시면 인증이 완료 됩니다. " + authUrl;
    MimeMessagePreparator msg = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
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
}
