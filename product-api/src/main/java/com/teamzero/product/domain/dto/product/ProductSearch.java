package com.teamzero.product.domain.dto.product;

import com.teamzero.product.domain.dto.product.NaverSearchDto.Response.NaverProduct;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ProductSearch {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request{

    @NotBlank
    private String keyword;
    @Builder.Default
    private int pageNumber = 1;
    @Builder.Default
    private int pageSize = 10;

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private int pageNumber;
    private int pageSize;
    private long total;
    private List<NaverProduct> content;

    public static ProductSearch.Response of(NaverSearchDto.Response response) {
      return Response.builder()
          .pageNumber(response.getPageNumber())
          .pageSize(response.getPageSize())
          .total(response.getTotal())
          .content(response.getContent())
          .build();
    }
  }

}
