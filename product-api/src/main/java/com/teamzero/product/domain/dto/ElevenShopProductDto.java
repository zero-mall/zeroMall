package com.teamzero.product.domain.dto;

import com.teamzero.product.domain.model.ProductOfMallEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElevenShopProductDto {
  private long productId;
  private String product_no;
  private String link_url;
  private String content_name;
  private String last_discount_price;
  private String mallId;
  private String imageUrl;

  public ProductOfMallEntity toProductOfMallEntity(){
    return ProductOfMallEntity.builder()
        .productId(this.productId)
        .mallId(this.mallId)
        .productName(this.content_name)
        .imgUrl(this.imageUrl)
        .linkUrl(this.link_url)
        .price(Long.parseLong(this.last_discount_price))
        .productMallId(this.product_no)
        .build();
  }

}
