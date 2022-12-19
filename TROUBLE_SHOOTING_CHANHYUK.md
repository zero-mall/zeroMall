# Trouble Shooting Record

#### 1. ServiceTest 중 오류발생
- Error
  - Test중 NullPointerException발생
 ```bash
java.lang.NullPointerException
        at com.teamzero.product.service.ReviewServiceTest.createReview(ReviewServiceTest.java:43)
 ```
- 원인 : ReviewService 클래스에 @Mock 어노테이션 주입
 ```java
    @Mock
    private ReviewService reviewService;
 ```

- 해결 : @Mock 어노테이션이 붙은 객체들을 주입시켜주는 @InjectMocks 어노테이션으로 변경하여 해결하였다
 ```java
    @InjectMocks
    private ReviewService reviewService;
 ```
#### 2. 스크래핑 중 키워드가 제대로 들어가지 않음
- Error
  - 스크래핑 중 키워드가 제대로 들어가지 않아서 잘못된 URL로 연결됨

- 원인 : 키워드를 string 그대로 주입
 ```java
    String url = String.format(URL, keyword);
 ```

-  해결 : URLEncoder.encode사용하여 UTF-8 형식에 맞게 변환하여 주입
 ```java
    String url = String.format(URL, URLEncoder.encode(keyword, "UTF-8")) ;

 ```

#### 3. 가격정보 저장 중 오류발생
- Error
  - 가격정보 저장 중 NumberFormatException발생
 ```bash
java.lang.NumberFormatException: For input string: "1,852,900"
	at java.base/java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
 ```
- 원인 : last_discount_price에 숫자가 아닌 문자열이 포함되어서 발생하였음
 ```
  "1,852,900"
 ```

-  해결 : build에 replace 추가
 ```java
     .replace(",", ""))
 ```

#### 4. Gmarket 이미지 URL 스크래핑 불가
- Error
  - 스크래핑 중 이미지 URL이 엉뚱한 주소로 불러와짐

- 원인 : 동적으로 작동하는 페이지는 jsoup으로 스크랩핑이 어려움.

- 해결 : 이미지 URL 형식을 파악하여 상품번호를 넣는방식으로 build중 주입함
 ```java
    .imageUrl("https://gdimg.gmarket.co.kr/" + mallProductId + "/still/280")
 ```
#### 5. Ak몰 상품번호 스크랩불가
- 원인 : AK몰 페이지상에서 상품번호 정보가 따로 존재하지않아서 스크래핑이 불가

- 해결 : 상품 URL에서 상품 번호를 추출하는 방식으로 해결
 ```java
   .product_no(productURL.replaceAll("[^0-9]", ""))

 ```