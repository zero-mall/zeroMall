package com.teamzero.product.domain.model;

import com.teamzero.product.domain.model.constants.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRegister {
  private String catName;
  private String parentCatId;
  private String catType;
  private String currentCatId;

  private CategoryType catTypeObject;

  //파라미터로 들어온 문자형타입의 카테고리타입을 카테고리타입으로 변경
  public void toCatTypeObject(){
    for(var item : CategoryType.values()){
      if(item.getDescription().equals(this.catType)){
        catTypeObject = item;
        break;
      }
    }
  }

}
