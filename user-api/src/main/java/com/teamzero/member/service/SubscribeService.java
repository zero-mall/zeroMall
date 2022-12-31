package com.teamzero.member.service;

import static com.teamzero.member.exception.ErrorCode.MEMBER_GRADE_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.teamzero.member.exception.ErrorCode.SUBSCRIBE_TASK_CAN_NOT_BE_DONE;

import com.teamzero.member.domain.model.MemberEntity;
import com.teamzero.member.domain.model.MemberGradeEntity;
import com.teamzero.member.domain.repository.MemberGradeRepository;
import com.teamzero.member.domain.repository.MemberRepository;
import com.teamzero.member.exception.TeamZeroException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;

    /**
     * 구독 등록
     * @param email
     * @param grade
     * @return
     */
    @Transactional
    public boolean add(String email, String grade) {
        //TODO 결제 성공시 진행
        try {
            // 결제 성공시 회원조회하여 등급수정
            MemberEntity member = memberRepository.findAllByEmail(email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
            member.setMemberGradeEntity(
                MemberGradeEntity.builder()
                    .member(member)
                    .name(grade)
                    .build());
            member.setSubscribedAt(LocalDateTime.now());
            member.setSubscribeYn(true);
        } catch (TeamZeroException e) {
            throw new TeamZeroException(SUBSCRIBE_TASK_CAN_NOT_BE_DONE);
        }
        return true;
    }

    /**
     * 구독 수정
     * @param email
     * @param subscribeId
     * @param grade
     * @return
     */
    @Transactional
    public boolean modify(String email, Long subscribeId,
        String grade) {
        //TODO 결제 성공시 진행
        try {
            // 결제 성공시 회원조회하여 등급수정
            MemberEntity member = memberRepository.findAllByEmail(email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
            MemberGradeEntity memberGrade = memberGradeRepository.findById(
                    subscribeId)
                .orElseThrow(() -> new TeamZeroException(
                    MEMBER_GRADE_NOT_FOUND));
            memberGrade.setName(grade);
            member.setMemberGradeEntity(memberGrade);
            member.setSubscribedAt(LocalDateTime.now());

        } catch (TeamZeroException e) {
            throw new TeamZeroException(SUBSCRIBE_TASK_CAN_NOT_BE_DONE);
        }
        return true;
    }

    /**
     * 구독 취소
     * @param email
     * @param subscribeId
     * @return
     */
    @Transactional
    public boolean cancel(String email, Long subscribeId) {
        try {
            // 회원조회하여 구독여부 false로 설정 후 MemberGradeEntity 삭제 처리
            MemberEntity member = memberRepository.findAllByEmail(email)
                .orElseThrow(() -> new TeamZeroException(MEMBER_NOT_FOUND));
            memberGradeRepository.deleteById(subscribeId);
            member.setSubscribeYn(false);
            member.setSubscribedAt(null);

        } catch (TeamZeroException e) {
            throw new TeamZeroException(SUBSCRIBE_TASK_CAN_NOT_BE_DONE);
        }
        return true;
    }
}
