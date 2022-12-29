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

public class AkmallScraper extends ScrapConfig implements
    ProductScraperInterface {

    private MallProductEntity mallProductEntity;
    private static final String SEARCH_URL = "https://www.akmall.com/search/Search2.do?search=";

    @Override
    public List<MallProductEntity> getScrapProductList(ProductEntity product) {
        List<MallProductEntity> mallProductEntities = new ArrayList<>();
        int price = product.getPrice();
        long maxPrice = (long) (price + Math.floor(price * super.TOLERANCE));
        long minPrice = (long) (price - Math.floor(price * super.TOLERANCE));

        // 가격 필터(저가순 정렬)
        String priceAdd = "&minPrice=" + minPrice + "&maxPrice=" + maxPrice
            + "&sort=BENEFIT_APPLY_PRICE%2FASC";
        Document document = null;
        try {
            Connection connection = Jsoup.connect(
                SEARCH_URL + URLEncoder.encode(product.getProductName(), "UTF-8")
                    + priceAdd);
            connection
                .header("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 상품명
        Iterator<Element> productName = document.select("a.name span")
            .iterator();
        // 상품가격
        Iterator<Element> productPrice = document.select("dd.price_n strong")
            .iterator();
        // 상품URL
        Iterator<Element> productLink = document.select("a.name").iterator();
        // 상품 사진 URL
        Iterator<Element> productImage = document.select("div.thumb img")
            .iterator();
        // 조회정보 존재하지 않을시 MallProductEntity List 반환
        if (!productName.hasNext()) {
            return mallProductEntities;
        }

        while (productName.hasNext()) {
            // 상품번호 얻기위해 상품URL 따로 선언
            String productURL = productLink.next().attr("abs:href");
            mallProductEntity = MallProductEntity.builder()
                // HTML상에서 상품번호를 따로 제공하지 않아서 상품 주소에서 가져오도록 헀습니다.
                .productMallId(productURL.replaceAll("[^0-9]", ""))
                .productId(product.getProductId())
                .name(productName.next().text())
                .price(Integer.parseInt(productPrice.next().text()
                    // ',' 들어갈 시 오류발생하여 replace
                    .replaceAll("[^0-9]", "")))
                .detailUrl(productURL)
                .imageUrl(productImage.next().attr("abs:src"))
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
        return "akmall";
    }

}
