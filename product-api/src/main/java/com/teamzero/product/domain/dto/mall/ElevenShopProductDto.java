package com.teamzero.product.domain.dto.mall;

import com.teamzero.product.domain.model.MallProductEntity;
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
  private String mallName;
  private String imageUrl;

  public MallProductEntity toProductOfMallEntity(){
    return MallProductEntity.builder()
        .productId(productId)
        .name(content_name)
        .imageUrl(imageUrl)
        .detailUrl(link_url)
        .price(Integer.parseInt(last_discount_price))
        .productMallId(product_no)
        .mallName(mallName) //수정필요
        .build();
  }
}