package com.teamzero.product.mapper;

import com.teamzero.product.domain.model.ProductEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgeGroupPreferProductMapper {

  List<ProductEntity> getAgeGroupPreferProducts(int myAgeGroup);

}
