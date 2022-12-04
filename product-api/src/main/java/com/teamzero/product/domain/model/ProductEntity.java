package com.teamzero.product.domain.model;

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
@Table(name = "PRODUCT")
public class ProductEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;



}
