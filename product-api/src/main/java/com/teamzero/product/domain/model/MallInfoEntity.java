package com.teamzero.product.domain.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MALL_INFO")
public class MallInfoEntity extends BaseEntity {

  // 쇼핑몰 구분 인덱스
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mall_id", nullable = false)
  private Long mallId;

  // 연결된 쇼핑몰 상품 상세 정보
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn
  private List<MallProductInfoEntity> mallProductInfo;

  private String name;

}