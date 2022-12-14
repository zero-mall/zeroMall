package com.teamzero.product.client;

import com.teamzero.product.domain.dto.TmonSearch;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "tmon", url = "https://search.tmon.co.kr/search")
public interface TmonSearchClient {

  @GetMapping
  ResponseEntity<String> searchProducts(@SpringQueryMap TmonSearch tmonSearch);

}
