# Trouble Shooting Record

#### 1. ServiceTest 중 오류발생
- Error
  - Test중 NullPointerException발생
 ```bash
java.lang.NullPointerException
        at com.teamzero.product.service.ReviewServiceTest.createReview(ReviewServiceTest.java:43)
 ```
(1) 원인 : ReviewService 클래스에 @Mock 어노테이션 주입
 ```java
    @Mock
    private ReviewService reviewService;
 ```

(2) 해결 : @Mock 어노테이션이 붙은 객체들을 주입시켜주는 @InjectMocks 어노테이션으로 변경하여 해결하였다
 ```java
    @InjectMocks
    private ReviewService reviewService;
 ```