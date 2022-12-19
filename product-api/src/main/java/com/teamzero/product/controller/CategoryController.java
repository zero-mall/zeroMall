package com.teamzero.product.controller;

import com.teamzero.product.domain.dto.category.CategoryRegister;
import com.teamzero.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cat")
public class CategoryController {
  private final CategoryService categoryService;

  /**
   * 카테고리 등록
   * @return
   */
  @PostMapping("/register")
  public ResponseEntity<?> categoryRegister(@RequestBody CategoryRegister request){

    return ResponseEntity.ok(categoryService.categoryRegister(request));
  }

  /**
   * 조건 별 카테고리 조회
   * 1. 카테고리Id, 카테고리명, 카테고리 타입으로 조회 가능
   */
  @PostMapping("/find")
  public ResponseEntity<?> categoryFind(@RequestBody CategoryRegister request){

    return ResponseEntity.ok(categoryService.categoryFind(request));
  }

  /**
   * 카테고리 전체조회
   */
  @PostMapping("/findAll")
  public ResponseEntity<?> categoryFindAll(){
    return ResponseEntity.ok(categoryService.categoryFindAll());
  }

  /**
   * 카테고리 삭제
   * 1. 카테고리 코드, 카테고리 타입 으로 삭제가능
   * 2. 하위카테고리가 있을경우 오류응답
   */
  @PostMapping("/delete")
  public ResponseEntity<?> categoryDelete(@RequestBody CategoryRegister request){

    return ResponseEntity.ok(categoryService.categoryDelete(request));
  }
  /**
   * 카테고리명 수정
   * 1.카테고리 코드로 수정 가능
   */
  @PostMapping("/modify")
  public ResponseEntity<?> categoryModify(@RequestBody CategoryRegister request){

    return ResponseEntity.ok(categoryService.categoryModify(request));
  }
}
