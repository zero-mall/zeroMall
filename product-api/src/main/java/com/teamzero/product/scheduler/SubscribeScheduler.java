package com.teamzero.product.scheduler;

import com.teamzero.product.client.RedisClient;
import com.teamzero.product.domain.dto.recommend.SubscriberDto;
import com.teamzero.product.domain.model.constants.CacheKey;
import com.teamzero.product.mapper.SubScribersMapper;
import com.teamzero.product.recommend.AgeGroupPreferProductRec;
import com.teamzero.product.recommend.LikeAndStarProductRec;
import com.teamzero.product.recommend.LowestPriceProductRec;
import com.teamzero.product.util.MailSender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeScheduler {

  private final SubScribersMapper subScribersMapper;

  private final RedisClient redisClient;

  private final MailSender mailSender;

  private final AgeGroupPreferProductRec ageGroupPreferProductRec;
  private final LowestPriceProductRec lowestPriceProductRec;
  private final LikeAndStarProductRec likeAndStarProductRec;


  /**
   * 매일 오전 2시에 구독자에게 메일 전송
   * - 레디스에서 상품 검색 기록 삭제
   * - 구독자에게 메일 전송
   */
  @Scheduled(cron = "${scheduler.product.cron}", zone = "Asia/Seoul")
  public void sendEmailToSubscriber(){

    // 레디스에서 상품 검색 기록 삭제
    redisClient.delete(CacheKey.NAVER_SEARCH);

    // 각 상품 추천 로직에 따라 상품 정보 가져오기
    // 1. 회원 데이터에서 오늘을 기점으로 한 달이 된 구독자 조회
    List<SubscriberDto> subscribers = subScribersMapper.getMonthlySubscribers();

    // 2. 각 구독자들에게 상품 추천
    String subject = "[제로몰] 월간 상품 추천";

    for (SubscriberDto subscriber : subscribers) {

      ageGroupPreferProductRec.setUserAge(subscriber.getAge());

      String text = mailSender.getSubcribeHTML(
         mailSender.getAgeGroupPreferHTMLContent(ageGroupPreferProductRec.recommendProducts()),
         mailSender.getLowestPriceProductHTMLContent(lowestPriceProductRec.recommendProducts()),
         mailSender.getLikeAndStarHTMLContent(likeAndStarProductRec.recommendProducts())
      );

      mailSender.sendMail(subscriber.getEmail(), subject, text);

    }

  }


}
