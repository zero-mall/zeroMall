package com.teamzero.member.domain.model;

import com.teamzero.member.domain.model.constants.MemberStatus;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MEMBER")
public class MemberEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    private String email;
    private int age;
    private String nickname;
    private String password;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private MemberGradeEntity memberGradeEntity;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;

    // 이메일 인증 관련
    private String emailAuthKey;
    private boolean emailAuthYn;
    private LocalDateTime emailAuthExpiredAt;

    // 포인트 관련
    @Audited
    private long currentPoint;

    // 구독 관련
    private boolean subscribeYn;
    private LocalDateTime subscribedAt;

}
