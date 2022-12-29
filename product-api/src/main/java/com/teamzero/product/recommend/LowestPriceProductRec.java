package com.teamzero.product.recommend;

import com.teamzero.product.domain.dto.product.RecommendDto;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import com.teamzero.product.domain.repository.MallProductRepository;
import com.teamzero.product.domain.repository.ProductRepository;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LowestPriceProductRec implements ProductRecInterface {

    private final ProductRepository productRepository;
    private final MallProductRepository mallProductRepository;

    /**
     * 모든상품을 돌면서 한달 이내 최대 낙폭의 상품 상위 5개 반환
     * @return
     */
    @Override
    public List<RecommendDto> recommendProducts() {


        List<ProductEntity> productEntities = productRepository.findAll();
        HashMap<Long, Double> productMap = new HashMap<>();
        List<RecommendDto> recommendList = new ArrayList<>();

        // 모든 상품의 갯수만큼 돌면서 퍼센트 계산
        for (int i = 0; i < productEntities.size(); i++) {

            long productId = productEntities.get(i).getProductId();
            RecommendDto recommendDtoTmp = currentPricePercentage(productId);
            recommendList.add(recommendDtoTmp);
            // productId 는 MallProductEntityId로 잠깐 사용
            productMap.put(productId, recommendDtoTmp.getPerc());

        }
        List<Long> keyList = new ArrayList<>(productMap.keySet());

        //Collections 의 sort 활용하여 퍼센테이지별로 정렬
        Collections.sort(keyList,
            (v1, v2) -> (productMap.get(v1).compareTo(productMap.get(v2))));

        // 가장 퍼센테이지가 높은 상품 5개만 RecommendList 로반환
        for (int i = 0; i < 5; i++) {
            Optional<MallProductEntity> product =
                mallProductRepository.findById(keyList.get(i));

            recommendList.add(RecommendDto.builder()
                .productId(product.get().getProductId())
                .productName(product.get().getName())
                .imageUrl(product.get().getImageUrl())
                .price(product.get().getPrice())
                .maxPrice(product.get().getMaxPrice())
                .perc(productMap.get(keyList.get(i)))
                .build());
        }

        return recommendList;

    }

    // 오늘 기준으로 한달 이내의 maxPrice인지 확인하는 메서드
    public boolean dateCompare(String modifiedAt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -1);
        String monthAgo = dateFormat.format(now.getTime());
        int compare = monthAgo.compareTo(modifiedAt);
        if (compare <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public RecommendDto currentPricePercentage(Long productId) {

        List<MallProductEntity> mallProductEntities =
            mallProductRepository.findAllByProductId(productId);

        HashMap<Long, Double> productMap = new HashMap<>();

        for (int i = 0; i < mallProductEntities.size(); i++) {

            long mallProductId = mallProductEntities.get(i).getId();
            int price = mallProductEntities.get(i).getPrice();
            int maxPrice = mallProductEntities.get(i).getMaxPrice();

            String priceUpdateDt = DateTimeFormatter.ofPattern("yyyyMMdd")
                .format(mallProductEntities.get(i).getPriceUpdateDt());

            //퍼센테이지 구하는 공식
            double perc =
                (double) price / (double) maxPrice * 100.0 - 100.0;

            // 0퍼센트 혹은 -100%인 경우를 제외하고 maxPrice 날짜가 한달 이내일 경우에만 put
            if (perc < 0 && perc > -100 && dateCompare(priceUpdateDt)) {
                productMap.put(mallProductId, perc);
            }
        }

        List<Long> keyList = new ArrayList<>(productMap.keySet());

        //Collections 의 sort 활용하여 퍼센테이지별로 정렬
        Collections.sort(keyList,
            (v1, v2) -> (productMap.get(v1).compareTo(productMap.get(v2))));

        // MallProductEntity Id 활용하여 상품특정
        Optional<MallProductEntity> product = mallProductRepository.findById(
            keyList.get(0));

        // RecommendDto활용하여 MallProductEntity Id와 퍼센테이지 반환
        return RecommendDto.builder()
            .productId(product.get().getId())
            .perc(productMap.get(
                keyList.get(0)))
            .build();

    }

}
