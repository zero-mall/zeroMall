package com.teamzero.product.domain.dto.mall;

import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.repository.MallRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElevenShopProductDto {

  private final MallRepository mallRepository;
  private long productId;
  private String product_no;
  private String link_url;
  private String content_name;
  private String last_discount_price;
  private long mallId;
  private String imageUrl;

  public MallProductEntity toProductOfMallEntity(){
    return MallProductEntity.builder()
        .productId(productId)
        .name(content_name)
        .imageUrl(imageUrl)
        .detailUrl(link_url)
        .price(Integer.parseInt(last_discount_price))
        .productMallId(product_no)
        .build();
  }
}