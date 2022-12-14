package com.teamzero.product.domain.model;

import com.teamzero.product.domain.model.constants.ProductOfMallId;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@AuditOverride(forClass = BaseEntity.class)
@Table(name = "MALL_PRODUCT_INFO")
@IdClass(ProductOfMallId.class)
public class ProductOfMallEntity extends BaseEntity implements Serializable {
  @Id
  @Column(name = "productId")
  private long productId;
  @Id
  @Column(name = "mallId")
  private String mallId;

  private String productName;
  private String imgUrl;
  private String linkUrl;
  private long price;
  private String productMallId;
}
