package com.teamzero.product.domain.model;

import com.teamzero.product.domain.model.constants.MallStatus;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MALL")
public class MallEntity extends BaseEntity {

  // 쇼핑몰 구분 인덱스
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mallId;
  private String name;
  private boolean schedulerYn;
}