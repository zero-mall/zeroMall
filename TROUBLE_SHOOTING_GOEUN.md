# Trouble Shooting Record

### 1. @WebMvcTest 중 에러 발생 - JPA metamodel must not be empty!
```bash
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaAuditingHandler': Cannot resolve reference to bean 'jpaMappingContext' while setting constructor argument; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jpaMappingContext': Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: JPA metamodel must not be empty!
```
 (1) 원인 : 아래와 같이 Application에 걸어준 Jpa 관련 Annotation을 WebMvc에서는 인식할 수 없기 때문 (WebMvc는 컨트롤러, 서비스에 관한 처리만 해준다.)
 ```java
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@EnableFeignClients
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
 ```
 (2) 해결 : (방법2 사용 후 다른 이슈로 에러 발생한 후 방법1로 변경)
 - 방법1 : JpaAuditing Config에 @EnableJpaAuditing을 따로 걸어두거나
 - 방법2 : WebMvc 테스트 클래스에 @MockBean(JpaMetamodelMappingContext.class)를 추가한다.

<br>

### 2. JPA Repository가 정상적으로 DI 되지 않음
```java
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in com.teamzero.member.application.SignInApplication required a bean of type 'com.teamzero.member.domain.repository.MemberRepository' that could not be found.


Action:

Consider defining a bean of type 'com.teamzero.member.domain.repository.MemberRepository' in your configuration.


Process finished with exit code 1

```
(1) 원인 : MSA에서 여러 개의 모듈들을 사용하면서, Entity와 Repository를 잘 인식하지 못한 걸로 보인다. 
<br>
rf. https://jinseobbae.github.io/jpa/2021/11/02/post-jpa-repository-di-error
(2) 해결 : 아래와 같이 JpaConfig에 @EnableJpaRepositories와 @EntityScan을 넣어주었다.
```java
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.teamzero.member.domain.repository"})
@EntityScan(basePackages = {"com.teamzero.member.domain.model"})
public class JpaConfig {
}
```

<br>

### 3. Build.Gradle 순환 참조
(1) 이슈 : dependency의 순환 참조
```bash
Circular dependency between the following tasks:
:product-api:classes
\--- :product-api:compileJava
     +--- :product-api:jar
     |    \--- :product-api:classes (*)
     \--- :user-api:jar
          \--- :user-api:classes
               \--- :user-api:compileJava
                    +--- :product-api:jar (*)
                    \--- :user-api:jar (*)
```
(2) 원인 : 말그대로 product-api -> user-api -> product-api 를 가리키면서 서로 순환 참조하는 이슈가 발생했다.
(3) 해결 : 코드를 열어보니, user-api 쪽 Build.gradle에 불일요한 코드가 있어 이 부분을 제거했고 이후 잘 동작하는 걸 확인했다.
```java

dependencies {
    implementation(project(path: ':teamzero-domain', configuration: 'default'))
    // implementation(project(path: ':product-api', configuration: 'default'))     // <<-- 제거
    implementation 'org.springframework.security:spring-security-crypto:5.7.5'
}

```

<br>

### 4. Jsoup으로 스크래핑 중 결과가 나타나지 않는 현상
(1) 원인 :     
```bash
- 클라이언트 사이드 랜더링(CSR, Client-Side Rendering)에 의해 기존 검색 주소로는 Jsoup 크롤링 불가
- CSR은 사이트에 요청이 있을 때, 서버는 데이터만 전달하고, 브라우저가 이 데이터를 렌더링을 통해 화면에 뿌려주는 방식이라고 한다.
- [F12]를 통해 확인해보니, 실제로 사이트 내부에 여러 개의 Ajax 요청들이 있는 걸 확인했다.
- Jsoup은 HTTP Request를 사용하는 라이브러리이기 때문에, Ajax로 요청하는 콘텐츠를 볼 수 없다.
```      
(2) 해결 방법 : 
```bash
- 해결1 : 다른 라이브러리인 Solenium을 통해 크롤링을 하는 방법이 있다. 
         > 하지만 이 방법은 모두가 Jsoup을 쓰고 공통의 인터페이스 메소드를 구현하는 
           지금 상황에서 별로 좋은 방법은 아닌 것 같다.   
- 해결2 : [F12]를 눌러 네트워크 로그를 통해 Ajax을 통해 불러오는 URL을 받아본다. 
         > 조금 편법이긴 하지만 이 방법을 사용하기로 했다.
```
(3) 참고 사이트 :              
- Jsoup으로 숨겨진 HTML 추출 https://kr.coderbridge.com/questions/7c3d2665ee66421ebe728c1f9af409fa      
- Jsoup으로 페이징 로딩, ajax 통신 데이터 스프래핑 https://private-yeri.tistory.com/10       
- Solenum 사용하기 https://heodolf.tistory.com/104

<br>

### 5. Redis에 ResponseEntity 타입은 저장되지 않는 이슈
(1) 오류 코드
java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [org.springframework.http.ResponseEntity]

(2) 원인
- ResponseEntity는 Serializable을 구현하고 있지 않아, Redis에 serialize되어 저장되지 않는다.
```java
@GetMapping("/search")
  @Cacheable(key = "#request.keyword", value = CacheKey.NAVER_SEARCH)
  public ResponseEntity<ProductSearch.Response> searchNaverProducts(ProductSearch.Request request) {

    // 1. 네이버 쇼핑 API에서 상품 검색
    var naverResponse = productService.searchNaverProducts(request);

    // 2. 네이버 응답 -> 제로콜 응답값으로 변환
    return ResponseEntity.ok(ProductSearch.Response.of(naverResponse));
  }
```

(3) 해결
- 아래와 같은 방식으로 커스텀 ResponseEntity를 쓸 수도 있다. (출처 : 스택오버플로우)
```java
public CustomeResponseEntity extends ResponseEntity implements Serializable {

    private static final long serialVersionUID = 7156526077883281625L;

    public CustomResponseEntity(HttpStatus status) {
        super(status);
    }

    public CustomResponseEntity(Object body, HttpStatus status) {
        super(body, status);
    }

    public CustomResponseEntity(MultiValueMap headers, HttpStatus status) {
        super(headers, status);
    }

    public CustomResponseEntity(Object body, MultiValueMap headers, HttpStatus status) {
        super(body, headers, status);
    }
}
```
- 하지만 나의 경우, 팀원들 모두가 ResponseEntity를 사용하고 있기 때문에, Controller에서 사용한 @Cacheable을 없애고,
  RedisTemplate을 써서 데이터를 저장하고 조회하기로 했다.

<br>

### 7. MSA에서 다른 api의 서비스 클래스를 읽어오지 못함 
```bash
Parameter 0 of constructor in com.teamzero.product.service.StarService required a bean of type 'com.teamzero.member.service.MemberService' that could not be found.
```
(1) 원인 : user-api의 MemberService 클래스 bean이 없어서 나타나는 오류 
(2) 해결 방법 : @ComponentScan을 통해서 member 패키지를 포함시키면 member에 설정한 bean을 가져올 수 있는데, 그렇게 하니 product-api에서 사용중인 다른 bean들과 충돌이 일어났다.
(3) 대안 : @EnableJpaRepositories와 @EntityScan의 패키지 설정을 통해 MemberService를 가져오지 않고, MemberRepository를 가져왔다.
```java
@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories(basePackages = {"com.teamzero.product.domain.repository", "com.teamzero.member.domain.repository"})
@EntityScan(basePackages = {"com.teamzero.product.domain.model", "com.teamzero.member.domain.model"})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
```

<br>

### 8. java.net.MalformedURLException: Illegal character in URL
(1) 원인 : URL문자열을 복사해 오면서 아래와 같이 개행문자가 들어가서 난 오류였다.
```java
private static final String SEARCH_URL = "https://search.wemakeprice.com/api/wmpsearch/api/v3.0/wmp-search/search.json\"\n"
      + "      + \"?searchType=DEFAULT&search_cate=top&keyword=%s&isRec=1&_service=5&_type=3&price=%d~%d";
```
(2) 해결 : "\n" 와 같은 문자를 제거해주었다.