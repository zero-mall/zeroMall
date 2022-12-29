package com.teamzero.product.mapper;

import com.teamzero.product.domain.dto.recommend.RecommendDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeAndStarProductMapper {

  List<RecommendDto> getLikeAndStarProducts();

}
