package com.teamzero.product.service;

import static com.teamzero.product.exception.ErrorCode.MALL_NOT_FOUND;
import static com.teamzero.product.exception.ErrorCode.PRODUCT_NOT_FOUND;
import com.teamzero.product.domain.dto.mall.PriceCompareDto;
import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.MallProductRepository;
import com.teamzero.product.domain.repository.MallRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.exception.TeamZeroException;
import com.teamzero.product.scraper.AkmallScraper;
import com.teamzero.product.scraper.DanawaScraper;
import com.teamzero.product.scraper.ElevenScraper;
import com.teamzero.product.scraper.GmarketScraper;
import com.teamzero.product.scraper.ProductScraperInterface;
import com.teamzero.product.scraper.TmonScraper;
import com.teamzero.product.scraper.WeMakeScraper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MallService {

  private final ProductRepository productRepository;

  private final MallRepository mallRepository;

  private final MallProductRepository mallProductRepository;

  private final ProductScraperInterface[] scrapers = {
      new ElevenScraper(), new GmarketScraper(), new TmonScraper(),
      new AkmallScraper(), new DanawaScraper(), new WeMakeScraper()
  };

  /**
   * 각 쇼핑몰로부터 스크래핑 결과 저장 & 최저가 순으로 결과 반환
   * - Mall DB에 저장된 쇼핑몰 정보가 하나도 없는 경우, 실패 응답
   * - 반환 타입 : ProductCompareDto
   */
  public PriceCompareDto compareProductsFromMalls(Long productId,
      int pageSize, int pageNumber){

    // 1. 특정 상품을 기준으로
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new TeamZeroException(PRODUCT_NOT_FOUND));

    // 2. 사용하려는 스크래퍼 구현체로부터
    for (var scraper : scrapers) {
      // 유사 상품을 스크래핑 후 mall_product DB에 저장
      saveSimilarProductsFromMallsByScraping(product, scraper);
    }

    // 3. 최저가 순으로 상품 정보를 정렬하여 반환
    return PriceCompareDto.fromEntity(
        mallProductRepository.findAllByProductId(
            PageRequest.of(pageNumber, pageSize), productId));

  }

  /**
   * 각 쇼핑몰에서 유사 상품 스크래핑 후 결과값 저장
   * - 스크래퍼 구현체의 SEARCH_URL 쇼핑몰명과 일치하는 mall 데이터가 없는 경우, 실패 응답
   * - 관련 정책 :
   *   mall DB의 name 데이터는 각 쇼핑몰 도메인 주소의 영문 쇼핑몰명이다.
   *   (ex. www.tmon.co.kr --> tmon)
   *   mall DB에 저장되지 않는 쇼핑몰로부터는 데이터를 가져오지 않는다.
   */
  private void saveSimilarProductsFromMallsByScraping(ProductEntity product,
      ProductScraperInterface scraper) {

    // mall DB에 저장되어 있는 쇼핑몰인지 확인
    MallEntity mall = mallRepository.findByName(scraper.getMallName())
        .orElseThrow(() -> new TeamZeroException(MALL_NOT_FOUND));

    // 유사 상품 스크래핑하여 (각 스크래퍼당 10개, 모든 쇼핑몰 합산 최대 60개)
    // * 참고 정책 :
    // - 스크래핑 반환 기준 기준 : 기준 상품과 가격 오차범위 5% 이내의 유사 상품들을 정확도 순으로 정렬하여 최대 10개 반환
    List<MallProductEntity> mallProducts = scraper.getScrapProductList(product);

    // mall_product DB에 저장
    for (MallProductEntity mallProduct : mallProducts) {
      mallProduct.setMallName(mall.getName());
      mallProductRepository.save(mallProduct);
    }
  }


}
