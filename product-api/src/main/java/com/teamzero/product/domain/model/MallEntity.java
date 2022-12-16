package com.teamzero.product.domain.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "MALL")
public class MallEntity extends BaseEntity {

  // 쇼핑몰 구분 인덱스
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mallId;

  //jisu - 쇼핑몰 테이블에 다른 정보는 필요없어보입니다.
  // 연결된 네이버 상품 정보
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn
//  private ProductEntity product;
//
//  // 연결된 쇼핑몰 상품 목록
//  @OneToMany(fetch = FetchType.LAZY)
//  @JoinColumn
//  private List<MallProductEntity> mallProducts;

  private String name;

}