package com.teamzero.product.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "MALL_PRODUCT_INFO")
public class MallProductInfoEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 연결된 쇼핑몰 사이트 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private MallInfoEntity mallInfo;

  // 연결된 상품 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private ProductEntity product;

  // 쇼핑물 상품 상세 정보
  private String name;
  private String imageUrl;
  private String detailUrl;
  private int price;

}