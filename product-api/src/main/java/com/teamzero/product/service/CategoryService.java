package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.CATEGORY_ID_FULL_ERROR;
import static com.teamzero.product.exception.ErrorCode.CATEGORY_NAME_DUPLICATE;
import static com.teamzero.product.exception.ErrorCode.CATEGORY_PARAMETER_ERROR;
import static com.teamzero.product.exception.ErrorCode.CATEGORY_PARANTID_ERROR;
import static com.teamzero.product.exception.ErrorCode.CATEGORY_SUB_DATA_EXISTS;

import com.teamzero.product.domain.model.CategoryEntity;
import com.teamzero.product.domain.model.CategoryRegister;
import com.teamzero.product.domain.model.constants.CategoryType;
import com.teamzero.product.domain.repository.CategoryRepository;
import com.teamzero.product.exception.TeamZeroException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  /**
   * 신규 카테고리 등록
   * 1. 카테고리명 중복 확인
   * 2. 부모 카테고리 코드 있는지 확인
   * 3. 신규 카테고리ID 체번
   * 4. 카테고리 등록
   */
  public CategoryEntity categoryRegister(CategoryRegister request){
    //카테고리 타입 문자열을 카테고리타입형태로 변경
    request.toCatTypeObject();

    //카테고리명 중복 확인
    if(categoryRepository.existsByCatName(request.getCatName())){
       throw new TeamZeroException(CATEGORY_NAME_DUPLICATE);
    }

    //파라미터 값 확인
    if(!this.isCheckCategoryParam(request)){
      throw new TeamZeroException(CATEGORY_PARAMETER_ERROR);
    }

    //max카테고리id 가져오기
    String maxCatId = this.getMaxCatId(request);
    //가져온 maxCatId에서 +1
    maxCatId = this.incrementCatId(maxCatId, request.getCatTypeObject());

    CategoryEntity category = CategoryEntity.builder()
        .catId(maxCatId)
        .catName(request.getCatName())
        .build();

    categoryRepository.save(category);
    return category;
  }

  /**
   * 파라미터 체크
   */
  private boolean isCheckCategoryParam(CategoryRegister request){
    if(request.getCatName().isEmpty()){
      return false;
    }
    if(Objects.isNull(request.getCatTypeObject())){
      return false;
    }
    //대분류가 아니고 부모카테코리id가 없는경우
    if(!Objects.equals(request.getCatTypeObject(), CategoryType.ATYPE) &&
        request.getParentCatId().isEmpty()){
      return false;
    }
    return true;
  }

  /**
   * max카테고리id 가져오기
   */
  private String getMaxCatId(CategoryRegister request){
    //카테고리 타입 확인 후 대분류가 아니면 부모카테고리 확인
    if(Objects.equals(request.getCatTypeObject(), CategoryType.ATYPE)){
      String result = categoryRepository.maxByCatIdAtype();
      return result == null? "000000000" : result;
    }else{//대분류가 아닌경우 부모카테고리Id가 있는지 체크
      if(!categoryRepository.existsByCatId(request.getParentCatId())){
        throw new TeamZeroException(CATEGORY_PARANTID_ERROR);
      }
      if(Objects.equals(request.getCatTypeObject(), CategoryType.BTYPE)){
        return  categoryRepository.maxByCatIdBtype(request.getParentCatId());
      }else{
        return  categoryRepository.maxByCatIdCtype(request.getParentCatId());
      }
    }
  }

  /**
   * max 카테고리id 가져와서 다음 순번 따기
   * 999다음은 a00순으로
   * @param maxCatId
   * @return
   */
  private String incrementCatId(String maxCatId, CategoryType type){
    //카운팅할 데이터 나누기
    String parentId;
    String childId;
    if(Objects.equals(type, CategoryType.ATYPE)){
      parentId = "";
      childId = maxCatId.substring(0,3);
    }else if(Objects.equals(type, CategoryType.BTYPE)){
      parentId = maxCatId.substring(0,3);
      childId = maxCatId.substring(3,6);
    }else{
      parentId = maxCatId.substring(0,6);
      childId = maxCatId.substring(6);
    }

    if(childId.equals("zzz")){ //더이상 카운팅할 코드가 없을 때
      throw new TeamZeroException(CATEGORY_ID_FULL_ERROR);
    }

    char[] childs = childId.toCharArray();

    int temp = (int) childs[2];

    //999인경우
    if(childs[0]=='9' && childs[1]=='9' && childs[2] =='9'){
      childs[0]='a';
      childs[1]='a';
      childs[2]='a';
    } else if(temp > 47 && temp < 58){  //숫자인경우
      if(temp==57){  //9인경우 자릿수 올림
        if(childs[1] == '9'){
          childs[0]+= childs[0];
          childs[1]+= '0';
          childs[2] = '0';
        }else{
          childs[1]+= childs[1];
          childs[2] = '0';
        }
      }else{
        childs[2] = (char)++temp;
      }
    }else{ //영문소문자인경우
      if(temp == 122){ //3번째 값이 z인경우
        if(childs[1] == 'z'){ //2번째도 z인경우
          childs[0]++;
          childs[1] = 'a';
          childs[2] = 'a';
        }else{
          childs[1]++;
          childs[2] = 'a';
        }
      }else{
        childs[2]++;
      }
    }

    String result = "";
    for(char item : childs){
      result = result.concat(String.valueOf(item));
    }

    if(Objects.equals(type, CategoryType.ATYPE)){
      result = result.concat("000000");
    }else if(Objects.equals(type, CategoryType.BTYPE)){
      result = parentId.concat(result).concat("000");
    }else{
      result = parentId.concat(result);
    }

    return result;
  }

  /**
   * 카테고리 조회
   * 1. 카테고리 id, 카테고리명 요청한 경우
   * 2. 카테고리명, 카테고리타입 요청한 경우
   * 3. 카테고리 id만 요청한 경우
   * 4. 카테고리명만 요청한 경우
   * 5. 카테고리타입만 요청한경우
   * 6. 해당사항이 없는 경우 null;
   * @param request
   * @return
   */
  public List<CategoryEntity> categoryFind(CategoryRegister request) {
    request.toCatTypeObject();
    String categoryTypeCode;
    //카테고리 타입이 들어왔는데 값이 잘못된 경우
    if(Objects.isNull(request.getCatTypeObject())
        && !Objects.isNull(request.getCatType()))
    {
      throw new TeamZeroException(CATEGORY_PARAMETER_ERROR);
    }

    //1.카테고리 id, 카테고리명 요청한 경우
    if(!Objects.isNull(request.getCurrentCatId()) &&
          !Objects.isNull(request.getCatName()))
      return categoryRepository.findAllByCatIdAndCatName
          (request.getCurrentCatId(), request.getCatName());

    if(Objects.equals(request.getCatTypeObject(), CategoryType.ATYPE)){
      categoryTypeCode = "%000000";
    }else{
      categoryTypeCode = "%000";
    }

    //2.카테고리명, 카테고리타입 요청한 경우
    if(!Objects.isNull(request.getCatName()) &&
        !Objects.isNull(request.getCatType())){
      //대분류 xxx000000, 중분류 xxxxxx000, 소분류 xxxxxxxxx
      if(Objects.equals(request.getCatTypeObject(), CategoryType.CTYPE)){
        return categoryRepository.findAllByCatNameAndCatIdNotLike
            (request.getCatName(), categoryTypeCode);
      }else if(Objects.equals(request.getCatTypeObject(), CategoryType.ATYPE)){
        return categoryRepository.findAllByCatNameAndCatIdLike
            (request.getCatName(), categoryTypeCode);
      }else{
        return categoryRepository.findAllByCatNameAndCatIdLikeCatIdNotLike
            (request.getCatName(), categoryTypeCode, "%000000");
      }
    }
    //3.카테고리Id로 조회한 경우
    if(!Objects.isNull(request.getCurrentCatId())){
      return categoryRepository.findAllByCatId(request.getCurrentCatId());
    }
    //4.카테고리명만 요청한 경우
    if(!Objects.isNull(request.getCatName())){
      return categoryRepository.findAllByCatName(request.getCatName());
    }
    //5. 카테고리타입만 요청한경우
    if(!Objects.isNull(request.getCatType())){
      if(Objects.equals(request.getCatTypeObject(), CategoryType.CTYPE)){
        return categoryRepository.findAllByCatIdNotLike(categoryTypeCode);
      }else if(Objects.equals(request.getCatTypeObject(), CategoryType.ATYPE)){
        return categoryRepository.findAllByCatIdLike(categoryTypeCode);
      }else{
        return categoryRepository.findAllByCatIdLikeCatIdNotLike
            (categoryTypeCode, "%000000");
      }
    }
    return null;
  }

  /**
   * 카테고리 전체 조회
   */
  public List<CategoryEntity> categoryFindAll(){
    return categoryRepository.findAll();
  }

  /**
   * 카테고리 삭제
   * 1. 카테고리Id가 정상Id인지 확인
   * 2. 대분류, 중분류일 때 하위카테고리가 있으면 에러
   * 3. 삭제
   * @param request
   * @return
   */
  public String categoryDelete(CategoryRegister request) {
    request.toCatTypeObject();

    if(Objects.isNull(request.getCatTypeObject())){
      throw new TeamZeroException(CATEGORY_PARAMETER_ERROR);
    }

    if(!categoryRepository.existsByCatId(request.getCurrentCatId())){
      throw new TeamZeroException(CATEGORY_PARAMETER_ERROR);
    }

    boolean isChildYn = false;

    //소분류는 하위 카테고리가 없으므로 바로 삭제
    if(Objects.equals(request.getCatTypeObject(), CategoryType.ATYPE)){
      isChildYn = categoryRepository.countByCatIdLike
          (request.getCurrentCatId().substring(0, 3).concat("%")) > 1;
    }else if(Objects.equals(request.getCatTypeObject(), CategoryType.BTYPE)){
      isChildYn = categoryRepository.countByCatIdLike
          (request.getCurrentCatId().substring(0, 6).concat("%")) > 1;
    }
    if(isChildYn){
      throw new TeamZeroException(CATEGORY_SUB_DATA_EXISTS);
    }

    categoryRepository.deleteById(request.getCurrentCatId());

    return "Category Delete Complete";
  }

  /**
   * 카테고리명 수정
   * 1. 카테고리ID없는 경우 오류
   * 2. 수정
   * @param request
   * @return
   */
  public CategoryEntity categoryModify(CategoryRegister request) {

    CategoryEntity category =
        categoryRepository.findByCatId(request.getCurrentCatId())
                .orElseThrow(() ->
                    new TeamZeroException(CATEGORY_PARAMETER_ERROR));

    category.setCatName(request.getCatName());
    categoryRepository.save(category);
    return category;
  }

}
