package com.teamzero.product.redis;

import com.teamzero.product.domain.dto.product.MallProductSearch.Response;
import com.teamzero.product.domain.model.constants.CacheKey;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@RedisHash(CacheKey.MALLPRODUCT_LIST)
public class RedisMallProduct {
  @Id
  private String id;

  private HashMap<Long, List<Response>> productMap = new HashMap<>();

}
