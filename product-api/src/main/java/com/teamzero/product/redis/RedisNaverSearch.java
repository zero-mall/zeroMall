package com.teamzero.product.redis;

import com.teamzero.product.domain.dto.product.ProductSearch;
import com.teamzero.product.domain.model.constants.CacheKey;
import javax.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(CacheKey.NAVER_SEARCH)
public class RedisNaverSearch {

  @Id
  String keyword;

  ProductSearch.Response response;

}
