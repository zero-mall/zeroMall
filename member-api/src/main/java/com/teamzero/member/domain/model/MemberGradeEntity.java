package com.teamzero.member.domain.model;

import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MEMBER_GRADE")
public class MemberGradeEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MemberEntity memberEntity;

    private int rewardPointPct; // 포인트 적립 비율

}
