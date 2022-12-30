package com.teamzero.product.util;

import static com.teamzero.product.exception.ErrorCode.MAIL_SEND_FAIL;

import com.teamzero.product.domain.dto.recommend.RecommendDto;
import com.teamzero.product.exception.TeamZeroException;
import java.util.List;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailSender {

  private final JavaMailSender javaMailSender;

  public boolean sendMail(String mail, String subject, String text) {

    boolean result = false;

    MimeMessagePreparator msg = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(mail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text);
      }
    };

    try {
      javaMailSender.send(msg);
      result = true;
    } catch(Exception e) {
      throw new TeamZeroException(MAIL_SEND_FAIL);
    }

    return result;
  }

  public String getSubscribeHTML(String ageGroupPreferHTMLContent,
      String lowestPriceProductHTMLContent, String likeAndStarHTMLContent){

    String template =
        "<!doctype html>\n"
            + "<html lang=ko>\n"
            + "<head>\n"
            + "  <meta charset=\"UTF-8\">\n"
            + "  <meta name=\"viewport\"\n"
            + "        content=\"width=device-width, user-scalable=no, "
            + "  initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n"
            + "  <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n"
            + "  <style>\n"
            + "    .box {width: 1070px; margin:auto; padding:10px; box-sizing: border-box}\n"
            + "    h2 { text-align: center }\n"
            + "    .content {width:100%;}\n"
            + "    .rec-box { height : 450px;}\n"
            + "    .product-list-box { height : 450px;}\n"
            + "    .product-list { list-style: none; padding : 0; }\n"
            + "    .product { width : 200px; height : 300px; position:relative}\n"
            + "    .product img { width : 200px; height: 300px; border-radius: 5px;}\n"
            + "    .product .tag { color: white; border-radius: 5px;\n"
            + "      padding : 5px; box-sizing: border-box; font-size: 12px;}\n"
            + "    .product .best-tag {background-color: black; position:absolute; top:10px; left:10px }\n"
            + "    .product .perc-tag {background-color: blue; position:absolute; top:10px; right:10px }\n"
            + "    .product-list li { float:left; margin-right : 10px;}\n"
            + "    .product .max-price { text-decoration: line-through; }\n"
            + "  </style>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <div class = \"box\">\n"
            + "    <h2>[제로몰] 월간 상품 추천</h2>\n"
            + "    <div class = \"content\">\n"
            + "      <div id = \"lowest-price-product\" class = \"rec-box\">\n"
            + "        <h3>이번 달 가장 많이 할인된 상품 5선</h3>\n"
            + lowestPriceProductHTMLContent
            + "      </div>\n"
            + "      <div id = \"age-prefer-rec\" class = \"rec-box\">\n"
            + "        <div class = \"product-list-box\">\n"
            + ageGroupPreferHTMLContent
            + "        </div>\n"
            + "      </div>\n"
            + "      <div id = \"like-and-star\" class = \"rec-box\">\n"
            + "        <h3>이번 달 우수 상품 5선</h3>\n"
            + likeAndStarHTMLContent
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</body>\n"
            + "</html>";

    return template;
  }

  public String getAgeGroupPreferHTMLContent(List<RecommendDto> recommendDtos){

    StringBuilder sb = new StringBuilder();

    sb.append(
        String.format("<h3>이번 달 <span>30</span>대's choice 상품 5선</h3>"
            + "<ul class = \"product-list\">",
        recommendDtos.get(0).getAgeGroup())
    );

    for (int i = 0; i < recommendDtos.size(); i++) {

      sb.append(
          String.format(
          "<li css = \"margin-right:0\"> "
              + "  <div class = \"product\"> "
              + "    <span class = \"tag best-tag\">BEST%d</span> "
              + "    <img src = \"%s\"> "
              + "    <div>\n"
              + "      <p class = \"product-name\">%s</p> "
              + "      <p class = \"product-price\">%d</p> "
              + "    </div> "
              + "  </div> "
              + "</li>",
            i + 1, recommendDtos.get(i).getImageUrl(),
            recommendDtos.get(i).getProductName(), recommendDtos.get(i).getPrice()
          )
      );

    }

    sb.append("</ul>\n");

    return sb.toString();
  }

  public String getLowestPriceProductHTMLContent(List<RecommendDto> recommendDtos){

    StringBuilder sb = new StringBuilder();

    sb.append("<ul class = \"product-list\">");

    for (int i = 0; i < recommendDtos.size(); i++) {

      sb.append(
          String.format(
              "<li css = \"margin-right:0\"> "
                  + "  <div class = \"product\"> "
                  + "    <span class = \"tag best-tag\">BEST%d</span> "
                  + "    <span class = \"tag perc-tag\">%d%%▼</span>"
                  + "    <img src = \"%s\"> "
                  + "    <div>\n"
                  + "      <p class = \"product-name\">%s</p> "
                  + "      <p class = \"product-price\">%d <span class = \"max-price\">%d</span></p> "
                  + "    </div> "
                  + "  </div> "
              + "</li>",
              i + 1, recommendDtos.get(i).getPerc(),
              recommendDtos.get(i).getImageUrl(),
              recommendDtos.get(i).getProductName(),
              recommendDtos.get(i).getPrice(), recommendDtos.get(i).getMaxPrice()
          )
      );

    }

    sb.append("</ul>\n");

    return sb.toString();

  }

  public String getLikeAndStarHTMLContent(List<RecommendDto> recommendDtos){

    StringBuilder sb = new StringBuilder();

    sb.append("<ul class = \"product-list\">");

    for (int i = 0; i < recommendDtos.size(); i++) {

      sb.append(
          String.format(
              "<li css = \"margin-right:0\"> "
                  + "  <div class = \"product\"> "
                  + "    <span class = \"tag best-tag\">BEST%d</span> "
                  + "    <img src = \"%s\"> "
                  + "    <div>\n"
                  + "      <p class = \"product-name\">%s</p> "
                  + "      <p class = \"product-price\">%d</p> "
                  + "    </div> "
                  + "  </div> "
                  + "</li>",
              i + 1, recommendDtos.get(i).getImageUrl(),
              recommendDtos.get(i).getProductName(), recommendDtos.get(i).getPrice()
          )
      );

    }

    sb.append("</ul>\n");

    return sb.toString();
  }

}
