package com.teamzero.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class View {

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Data
  public static class Request{

    private String catId;
    private Long productId;

  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @Getter
  @Setter
  public static class Response{

    private long totalCnt;
    private double avgTotalView;
    private CatView catView = new CatView("카테고리 정보 미요청", -1, -1);
    private ProductView productView = new ProductView(null, "상품 정보 미요청", -1);

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CatView{

      private String catId;
      private long totalCatCnt;
      private double avgCatView;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class ProductView{

      private Long productId;
      private String productName;
      private long productView;

    }

  }

}
