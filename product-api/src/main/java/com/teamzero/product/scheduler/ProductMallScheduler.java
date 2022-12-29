package com.teamzero.product.scheduler;

import com.teamzero.product.client.RedisClient;
import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.dto.product.MallProductSearchDto;
import com.teamzero.product.domain.dto.product.MallProductSearchDto.Response;
import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.domain.repository.MallProductRepository;
import com.teamzero.product.domain.repository.MallRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import com.teamzero.product.scraper.AkmallScraper;
import com.teamzero.product.scraper.DanawaScraper;
import com.teamzero.product.scraper.ElevenScraper;
import com.teamzero.product.scraper.GmarketScraper;
import com.teamzero.product.scraper.ProductScraperInterface;
import com.teamzero.product.scraper.TmonScraper;
import com.teamzero.product.scraper.WeMakeScraper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductMallScheduler {

  /**
   * 멀티스레드를 사용하기 위해서 쇼핑몰별 스케쥴러 분리
   */
  private final ProductRepository productRepository;
  private final MallProductRepository mallProductRepository;
  private final MallRepository mallRepository;
  private final RedisClient redisClient;

  // 11번가,지마켓,티몬,Ak몰,다나와,위메프
  /**
   * 스크래핑이 완료된 쇼핑몰 Redis에 저장
   * 1. 모든 쇼핑몰 스크래핑 완료 체크
   * 2. 모든 쇼핑몰 스크래핑 끝났으면 Redis데이터 삭제
   * 3. 해당 쇼핑몰자료 redis에 저장
   * 4. mallEntity에 schedulerYn컬럼 false체크
   */
  @Scheduled(cron = "${scheduler.redis.cron}", zone = "Asia/Seoul")
  public void setRedisMallProduct(){
    if(!mallRepository.existsBySchedulerYn(false)){
      deleteRedisMallProduct();
      registerRedisMallProduct();
    }
  }

  /**
   * 1. 메인상품 리스트 가져오기
   * 2. 상품별로 스크래핑해서 데이터 가져오기
   * 3. 상품데이터 RDB에 저장
   * 4. Redis에 저장하기 위해 스크래핑 완료 체크
   */
  //cron = 초 분 시 일 월 주 년(생략가능)
  @Scheduled(cron = "${scheduler.mallproduct.cron}", zone="Asia/Seoul")
  public void akMallScheduler(){
    String logPrefix = "AkMall Scheduler ";
    log.info(logPrefix + "Start");
    List<ProductEntity> productEntities = productRepository.findAll();
    log.info("Get Product List "
        + "---> size = " + productEntities.size());

    ProductScraperInterface scraper = new AkmallScraper();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductEntities;
      mallProductEntities = scraper.getScrapProductList(item);
      if(!ObjectUtils.isEmpty(mallProductEntities)){
        registerMallProduct(mallProductEntities);
      }
    }
    MallEntity mall =
        mallRepository.findAllBymallId(ScrapConfig.AK_MALLID).get();
    mall.setSchedulerYn(true);
    mallRepository.save(mall);
    log.info(logPrefix + "End");
  }

  @Scheduled(cron = "${scheduler.mallproduct.cron}", zone="Asia/Seoul")
  public void elevenScheduler(){
    String logPrefix = "Eleven Scheduler ";
    log.info(logPrefix + "Start");
    List<ProductEntity> productEntities = productRepository.findAll();
    log.info("Get Product List "
        + "---> size = " + productEntities.size());

    ProductScraperInterface scraper = new ElevenScraper();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductEntities;
      mallProductEntities = scraper.getScrapProductList(item);
      if(!ObjectUtils.isEmpty(mallProductEntities)){
        registerMallProduct(mallProductEntities);
      }
    }
    MallEntity mall =
        mallRepository.findAllBymallId(ScrapConfig.ELEVENSHOP_MALLID).get();
    mall.setSchedulerYn(true);
    mallRepository.save(mall);
    log.info(logPrefix + "End");
  }

  @Scheduled(cron = "${scheduler.mallproduct.cron}", zone="Asia/Seoul")
  public void tmonScheduler(){
    String logPrefix = "Tmon Scheduler ";
    log.info(logPrefix + "Start");
    List<ProductEntity> productEntities = productRepository.findAll();
    log.info("Get Product List "
        + "---> size = " + productEntities.size());

    ProductScraperInterface scraper = new TmonScraper();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductEntities;
      mallProductEntities = scraper.getScrapProductList(item);
      if(!ObjectUtils.isEmpty(mallProductEntities)){
        registerMallProduct(mallProductEntities);
      }
    }
    MallEntity mall =
        mallRepository.findAllBymallId(ScrapConfig.TMON_MALLID).get();
    mall.setSchedulerYn(true);
    mallRepository.save(mall);
    log.info(logPrefix + "End");
  }

  @Scheduled(cron = "${scheduler.mallproduct.cron}", zone="Asia/Seoul")
  public void gmarketScheduler(){
    String logPrefix = "Gmarket Scheduler ";
    log.info(logPrefix + "Start");
    List<ProductEntity> productEntities = productRepository.findAll();
    log.info("Get Product List "
        + "---> size = " + productEntities.size());

    ProductScraperInterface scraper = new GmarketScraper();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductEntities;
      mallProductEntities = scraper.getScrapProductList(item);
      if(!ObjectUtils.isEmpty(mallProductEntities)){
        registerMallProduct(mallProductEntities);
      }
    }
    MallEntity mall =
        mallRepository.findAllBymallId(ScrapConfig.GSHOP_MALLID).get();
    mall.setSchedulerYn(true);
    mallRepository.save(mall);
    log.info(logPrefix + "End");
  }

  @Scheduled(cron = "${scheduler.mallproduct.cron}", zone="Asia/Seoul")
  public void danawaScheduler(){
    String logPrefix = "Danawa Scheduler ";
    log.info(logPrefix + "Start");
    List<ProductEntity> productEntities = productRepository.findAll();
    log.info("Get Product List "
        + "---> size = " + productEntities.size());

    ProductScraperInterface scraper = new DanawaScraper();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductEntities;
      mallProductEntities = scraper.getScrapProductList(item);
      if(!ObjectUtils.isEmpty(mallProductEntities)){
        registerMallProduct(mallProductEntities);
      }
    }
    MallEntity mall =
        mallRepository.findAllBymallId(ScrapConfig.DANAWA_MALLID).get();
    mall.setSchedulerYn(true);
    mallRepository.save(mall);
    log.info(logPrefix + "End");
  }

  @Scheduled(cron = "${scheduler.mallproduct.cron}", zone="Asia/Seoul")
  public void wemakeScheduler(){
    String logPrefix = "WeMake Scheduler ";
    log.info(logPrefix + "Start");
    List<ProductEntity> productEntities = productRepository.findAll();
    log.info("Get Product List "
        + "---> size = " + productEntities.size());

    ProductScraperInterface scraper = new WeMakeScraper();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductEntities;
      mallProductEntities = scraper.getScrapProductList(item);
      if(!ObjectUtils.isEmpty(mallProductEntities)){
        registerMallProduct(mallProductEntities);
      }
    }
    MallEntity mall =
        mallRepository.findAllBymallId(ScrapConfig.WEMAP_MALLID).get();
    mall.setSchedulerYn(true);
    mallRepository.save(mall);
    log.info(logPrefix + "End");
  }

  /**
   * 1. 스크래핑된 상품몰별 상품List Parameter로 가져오기
   * 2. 몰상품id로 기존 데이터 있는지 체크하여 없으면 새로 등록, 있으면 데이터 업데이트
   */
  private void registerMallProduct(List<MallProductEntity> productEntities){
    for(MallProductEntity item : productEntities){
      Optional<MallProductEntity> mallProductEntityOptional =
          mallProductRepository.findAllByProductMallIdAndMallName
              (item.getProductMallId(), item.getMallName());

      MallProductEntity mallProductEntity;
      /**
       * 1.기존 데이터가 있을 때 기존 max가격과 새로운
       *   가격 비교하여 큰값 maxPrice에 저장
       * 2.데이터가 없을 때 스크래핑으로 가져온 데이터 저장
       */
      if(mallProductEntityOptional.isPresent()){
        mallProductEntity = mallProductEntityOptional.get();
        if(item.getPrice() > mallProductEntity.getMaxPrice()){
          mallProductEntity.setMaxPrice(item.getPrice());
          mallProductEntity.setPriceUpdateDt(LocalDate.now());
        }
        mallProductEntity.setName(item.getName());
        mallProductEntity.setDetailUrl(item.getDetailUrl());
        mallProductEntity.setImageUrl(item.getImageUrl());

        mallProductEntity.setPrice(item.getPrice());
        mallProductRepository.save(mallProductEntity);
      } else{
        item.setMaxPrice(item.getPrice());
        item.setPriceUpdateDt(LocalDate.now());
        mallProductRepository.save(item);
      }
    }
  }

  /**
   * 1. 스크래핑이 완료된 상품몰List Parameter로 가져오기
   * 2. 상품몰별 상품리스트 상품코드별 조회
   * 2. Redis 기존 데이터 삭제
   * 3. Redis에 저장
   */
  private void registerRedisMallProduct(){
    List<ProductEntity> productEntities = productRepository.findAll();
    for(ProductEntity item : productEntities){
      List<MallProductEntity> mallProductList =
          mallProductRepository.findAllByProductId(item.getProductId());
      if(mallProductList.size() == 0){
        continue;
      }
      List<Response> responseList = new ArrayList<>();

      for(MallProductEntity mallProductEntity : mallProductList){

        MallProductSearchDto.Response response = Response.builder()
            .name(mallProductEntity.getName())
            .imageUrl(mallProductEntity.getImageUrl())
            .detailUrl(mallProductEntity.getDetailUrl())
            .price(mallProductEntity.getPrice())
            .mallName(mallProductEntity.getMallName())
            .build();

        responseList.add(response);
      }
      HashMap<Long, List<Response>> redisHash = new HashMap<>();
      redisHash.put(item.getProductId(), responseList);
      redisClient.addData(CacheKey.MALLPRODUCT_LIST, redisHash);
    }
  }

  //쇼핑몰별 상품들 Redis에서 삭제
  private void deleteRedisMallProduct() {
    redisClient.delete(CacheKey.MALLPRODUCT_LIST);
  }
}
