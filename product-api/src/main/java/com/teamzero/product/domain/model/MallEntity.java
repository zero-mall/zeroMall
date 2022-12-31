package com.teamzero.product.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Data
@Entity
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