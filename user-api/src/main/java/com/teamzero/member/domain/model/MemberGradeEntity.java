package com.teamzero.member.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MEMBER_GRADE")
public class MemberGradeEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private MemberEntity member;

    private String name;

    private int rewardPointPct; // 포인트 적립 비율

}
