package com.teamzero.product.scraper;

import com.teamzero.product.config.ScrapConfig;
import com.teamzero.product.domain.model.MallEntity;
import com.teamzero.product.domain.model.MallProductEntity;
import com.teamzero.product.domain.model.ProductEntity;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DanawaScraper
    extends ScrapConfig implements ProductScraperInterface{

  static private final String SEARCH_URL =
      "https://search.danawa.com/dsearch.php?"
          + "query=%s&minPrice=%d&maxPrice=%d&sort=priceASC";
  @Override
  public List<MallProductEntity> getScrapProductList(ProductEntity product) {

    long price = product.getPrice();
    long maxPrice = (long) (price + Math.floor(price * TOLERANCE));
    long minPrice = (long) (price - Math.floor(price * TOLERANCE));

    List<MallProductEntity> mallProductEntities = new ArrayList<>();

    //Jsoup을 이용해서 11번가 조회 후 일반상품에 있는 데이터 가져오기
    String encodeParam =  URLEncoder.encode
        (product.getProductName(), StandardCharsets.UTF_8);

    Connection connection =
        Jsoup.connect(String.format(SEARCH_URL,encodeParam, minPrice, maxPrice));
    try {
      Document document = connection.get();

      Elements elements = document.getElementsByAttributeValue
          ("class","product_list");

      //mallProductEntities에 값을 넣기위한 index
      int idx = 0;
      //썸네일이미지
      for (Element value : elements.select("div.thumb_image img")) {
        String imgUrl = value.attr("src");
        if(!imgUrl.startsWith("//img.danawa.com/prod_img")){
          continue;
        }
        mallProductEntities.add(new MallProductEntity());
        imgUrl = "http:".concat(imgUrl);
        mallProductEntities.get(idx).setImageUrl(imgUrl);
        //공통값 넣기
        mallProductEntities.get(idx).setProductId(product.getProductId());
        mallProductEntities.get(idx++).setMallName(getMallName());
      }

      idx = 0;

      elements = elements.select("p.prod_name");
      //상품명, 상세링크
      for (Element value : elements) {
        if (value.getElementsByClass
            ("click_log_product_standard_title_").size() == 0) {
          continue;
        }
        String linkUrl = value.select("a").get(0).attr
            ("href");
        String name = value.select("a").get(0).text();
        mallProductEntities.get(idx).setDetailUrl(linkUrl);
        mallProductEntities.get(idx++).setName(name);
      }
      idx = 0;

      //금액, 상품번호
      elements = document.getElementsByAttributeValue("class",
          "rank_one");
      for (Element value : elements) {
        if (value.select("strong").size() <= 1) {
          continue;
        }
        //금액
        int productPrice = Integer.parseInt(value.select("strong").
            get(1).text().replaceAll(",", ""));
        //상품번호
        String productId =
            value.select("input").get(1).attr("value");
        mallProductEntities.get(idx).setPrice(productPrice);
        mallProductEntities.get(idx++).setProductMallId(productId);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if(mallProductEntities.size() > 10){
      List<MallProductEntity> resultList = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        resultList.add(i, mallProductEntities.get(i));
      }
      return resultList;
    }

    return mallProductEntities;
  }

  @Override
  public String getMallName() {
    return "danawa";
  }

}
