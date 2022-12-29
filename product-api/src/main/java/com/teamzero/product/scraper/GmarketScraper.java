package com.teamzero.product.scraper;

import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GmarketScraper extends ScrapConfig implements
    ProductScraperInterface {

    private MallProductEntity mallProductEntity;
    private static final String SEARCH_URL = "https://browse.gmarket.co.kr/search?keyword=";

    @Override
    public List<MallProductEntity> getScrapProductList(ProductEntity product) {
        List<MallProductEntity> mallProductEntities = new ArrayList<>();
        int price = product.getPrice();
        long maxPrice = (long) (price + Math.floor(price * super.TOLERANCE));
        long minPrice = (long) (price - Math.floor(price * super.TOLERANCE));

        // 가격 필터(저가순 정렬)
        String priceAdd = "&f=p:" + minPrice + "%5E" + maxPrice + "&s=1";

        Document document = null;
        try {
            Connection connection = Jsoup.connect(
                SEARCH_URL + URLEncoder.encode(
                    product.getProductName(), "UTF-8") + priceAdd
            );
            System.out.println(SEARCH_URL + URLEncoder.encode(
                product.getProductName(), "UTF-8") + priceAdd);
            connection
                .header("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();

        }

        // 상품명
        Iterator<Element> productName = document.select("span.text__item")
            .iterator();
        // 상품 가격
        Iterator<Element> productPrice = document.select(
            "div.box__price-seller strong").iterator();
        // 상품 URL
        Iterator<Element> productLink = document.select("div.box__image a")
            .iterator();
        // 해당 쇼핑몰 상품번호
        Iterator<Element> productId = document.select("div.box__image a")
            .iterator();
        // 조회정보 존재하지 않을시 MallProductEntity List 반환
        if (!productName.hasNext()) {
            return mallProductEntities;
        }

        while (productName.hasNext()) {
            // 사진URL에도 사용하기위해 선언
            String mallProductId = productId.next()
                .attr("data-montelena-goodscode");
            mallProductEntity = MallProductEntity.builder()
                .productMallId(mallProductId)
                .mallName(getMallName())
                .productId(product.getProductId())
                .name(productName.next().text())
                .price(Integer.parseInt(productPrice.next().text()
                    // ',' 들어갈 시 오류발생하여 replace
                    .replace(",", "")))
                .detailUrl(productLink.next().attr("abs:href"))
                //해당 사이트에서 동적으로 작동하는부분이라 이미지 URL불러오기가 불가능하여 추가했습니다.
                .imageUrl("https://gdimg.gmarket.co.kr/" + mallProductId
                    + "/still/280")
                .build();
            mallProductEntities.add(mallProductEntity);

            // 10개의 상품정보까지만 반환
            if (mallProductEntities.size() == 10) {
                return mallProductEntities;
            }
        }
        return mallProductEntities;
    }

    @Override
    public String getMallName() {
        return "gmarket";
    }

}
