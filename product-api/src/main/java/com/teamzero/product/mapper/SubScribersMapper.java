package com.teamzero.product.mapper;

import com.teamzero.product.domain.dto.recommend.SubscriberDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubScribersMapper {

  List<SubscriberDto> getMonthlySubscribers();

}
