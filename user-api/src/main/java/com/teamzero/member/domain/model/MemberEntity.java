package com.teamzero.member.domain.model;

import com.teamzero.member.domain.model.constants.MemberStatus;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
