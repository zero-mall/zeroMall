package com.teamzero.member.domain.model;

import com.teamzero.member.domain.model.constants.AdminStatus;
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
@Table(name = "ADMIN")
public class AdminEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    private String email;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private AdminStatus adminStatus;

}
