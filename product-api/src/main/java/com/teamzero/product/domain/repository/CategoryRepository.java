package com.teamzero.product.domain.repository;

import com.teamzero.product.domain.model.CategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

  boolean existsByCatName(String catName);
  int countByCatIdLike(String catId);
  boolean existsByCatId(String catId);
  Optional<CategoryEntity> findByCatId(String catId);

  /**
   * 카테고리 조회
   * @param catId
   * @param catName
   * @return
   */
  List<CategoryEntity> findAllByCatIdAndCatName(String catId, String catName);
  List<CategoryEntity> findAllByCatNameAndCatIdNotLike
      (String catName, String catId);
  List<CategoryEntity> findAllByCatNameAndCatIdLike
      (String catName, String catId);

  List<CategoryEntity> findAllByCatId(String catId);
  List<CategoryEntity> findAllByCatName(String catName);
  List<CategoryEntity> findAllByCatIdLike(String catId);
  List<CategoryEntity> findAllByCatIdNotLike(String catId);
  @Query("select cat "
      + "from CategoryEntity as cat "
      + "where cat.catId like :catId and cat.catId not like :notCatId")
  List<CategoryEntity> findAllByCatIdLikeCatIdNotLike(String catId, String notCatId);

  @Query("select cat "
      + "from CategoryEntity as cat "
      + "where cat.catName = :catName "
      + "and cat.catId like :catId and cat.catId not like :notCatId")
  List<CategoryEntity> findAllByCatNameAndCatIdLikeCatIdNotLike
      (String catName, String catId, String notCatId);

  /**
   * 카테고리 타입별 최대카테고리값 가져오기
   * @return
   */
  @Query("select max(cat.catId) as maxCatId "
      + "from CategoryEntity as cat where cat.catId like '%000000'")
  String maxByCatIdAtype();

  @Query(value = "select max(cat.catId) as maxCatId "
      + "from CategoryEntity as cat "
      + "where cat.catId like concat(substring(:parentId,1,3),'%')")
  String maxByCatIdBtype(@Param("parentId") String parentId);

  @Query(value = "select max(cat.catId) as maxCatId "
      + "from CategoryEntity as cat "
      + "where cat.catId like concat(substring(:parentId,1,6),'%')")
  String maxByCatIdCtype(String parentId);

}
