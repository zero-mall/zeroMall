package com.teamzero.product.redis;

import com.teamzero.product.domain.dto.ProductDetail;
import com.teamzero.product.domain.model.constants.CacheKey;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@RedisHash(CacheKey.NAVER_PRODUCT)
public class RedisProducts {

  @Id
  LocalDate id;

  private Map<String, ProductDetail.Response> productMap = new HashMap<>();

}
