package com.teamzero.product.domain.dto;

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
        .productId(this.productId)
        .name(this.content_name)
        .imageUrl(this.imageUrl)
        .detailUrl(this.link_url)
        .price(Integer.parseInt(this.last_discount_price))
        .productMallId(this.product_no)
        .mall(MallEntity.builder().mallId(this.mallId).name("11번가").build()) //수정필요
        .build();
  }
}