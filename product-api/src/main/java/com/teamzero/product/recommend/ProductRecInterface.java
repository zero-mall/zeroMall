package com.teamzero.product.recommend;

import com.teamzero.product.domain.dto.recommend.RecommendDto;
import java.util.List;

public interface ProductRecInterface {

  List<RecommendDto> recommendProducts();

}
