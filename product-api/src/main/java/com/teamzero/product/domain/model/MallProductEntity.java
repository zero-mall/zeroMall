package com.teamzero.product.domain.model;

import java.time.LocalDate;
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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MALL_PRODUCT")
@Data
public class MallProductEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 연결된 쇼핑몰
  private Long mallId;

  // 쇼핑물 상품 정보
  private String name;
  private String imageUrl;
  private String detailUrl;
  private int price;

  //상품 관리 key
  private long productId;
  //쇼핑몰별 상품번호
  private String productMallId;
  private int maxPrice;
  private LocalDate priceUpdateDt;
}