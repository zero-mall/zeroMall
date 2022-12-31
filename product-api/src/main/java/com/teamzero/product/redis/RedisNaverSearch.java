package com.teamzero.product.redis;

import com.teamzero.product.domain.dto.product.ProductSearchDto;
import com.teamzero.product.domain.model.constants.CacheKey;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@RedisHash(CacheKey.NAVER_SEARCH)
public class RedisNaverSearch {

  @Id
  String keyword;

  ProductSearchDto.Response response;

}
