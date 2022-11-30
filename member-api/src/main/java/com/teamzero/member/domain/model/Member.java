package com.teamzero.member.domain.model;

import com.teamzero.member.domain.model.constants.MemberRole;
import com.teamzero.member.domain.model.constants.MemberStatus;
import com.teamzero.member.domain.model.constants.RegisterType;
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
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private RegisterType registerType;

    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private MemberGrade memberGrade;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;

    // 이메일 인증 관련
    private String emailAuthKey;
    private boolean emailAuthYn;
    private LocalDateTime emailAuthExpiredAt;

    // 포인트 관련
    @Audited
    private long points; // 보유 포인트

}
