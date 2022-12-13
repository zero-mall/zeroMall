package com.teamzero.product.domain.dto;

import static com.teamzero.product.domain.model.constants.NaverSort.SIM;

import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

/**
 * 네이버 상품 조회 api document
 * https://developers.naver.com/docs/serviceapi/search/shopping/shopping.md
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaverSearch{

  // 검색어 (UTF-8)
  private String query;

  // 페이징 관련
  private Integer display; // 페이지 내 갯수
  private Integer start;   // 페이지 시작 지점

  // 검색 결과 정렬
  private String sort;

  public static NaverSearch getInstance(String searchQuery, Pageable pageable){

    String sortType = SIM.name().toLowerCase(Locale.ROOT);

    return NaverSearch.builder()
        .query(searchQuery)
        .display(pageable.getPageSize())
        .start(pageable.getPageNumber() + 1)
        .sort(sortType)
        .build();
  }

}
