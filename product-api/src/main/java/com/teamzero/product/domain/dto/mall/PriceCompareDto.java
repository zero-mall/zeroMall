package com.teamzero.product.domain.dto.mall;

import com.teamzero.product.domain.model.MallProductEntity;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
public class PriceCompareDto {

  long totalCnt;
  long totalPage;
  int pageSize;
  int pageNumber;
  List<ProductDto> content;

  public static PriceCompareDto fromEntity(Page<MallProductEntity> products) {
    return PriceCompareDto.builder()
        .totalCnt(products.getTotalElements())
        .totalPage(products.getTotalPages())
        .pageNumber(products.getNumber())
        .pageSize(products.getSize())
        .content(products.getContent().stream()
            .map(PriceCompareDto.ProductDto::fromEntity)
            .sorted(Comparator.comparingInt(ProductDto::getPrice))
            .collect(Collectors.toList()))
        .build();
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class ProductDto{

    private String mallName;
    private String productName;
    private int price;
    private String imageUrl;
    private String detailUrl;

    public static ProductDto fromEntity(MallProductEntity mallProduct) {
      return ProductDto.builder()
          .mallName(mallProduct.getMallName())
          .productName(mallProduct.getName())
          .price(mallProduct.getPrice())
          .imageUrl(mallProduct.getImageUrl())
          .detailUrl(mallProduct.getDetailUrl())
          .build();
    }
  }

}
