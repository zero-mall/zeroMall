package com.teamzero.product.domain.dto;

import com.teamzero.product.domain.dto.NaverSearch.Response.NaverProduct;
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
    private int pageNumber = 1;
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

    public static ProductSearch.Response of(NaverSearch.Response response) {
      return Response.builder()
          .pageNumber(response.getPageNumber())
          .pageSize(response.getPageSize())
          .total(response.getTotal())
          .content(response.getContent())
          .build();
    }
  }

}
