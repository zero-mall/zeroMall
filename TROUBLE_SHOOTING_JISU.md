# Trouble Shooting Record

#### 1.CATEGORY-ADD Barnch
- Error
  - Reqeust Parameter값에 널 값이 들어올때 널 확인 부분오류

- 원인 : isEmpty()로 NUll값 체크
 ```java
    if(parameter.temp.isEmpty()){
      return "null";
    }
 ```
- 해결 : isEmpty()는 값의 길이를 체크하여 길이가 0이면 true 아니면 false를 리턴하는 메소드이다.
값자체가 NULL이면 길이를 가져올수 없으므로 에러가 난다. 
 ```java
  if(Objects.isNull((parameter.temp)){
    return true;
    }
 ```
Objects.isNull()이용하여 널체크 하였다.

#### 2.ProductOfMallEntity추가 에러
- Error
  - Failed to initialize JPA EntityManagerFactory: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is org.hibernate.MappingException: Composite-id class must implement Serializable: com.teamzero.product.domain.model.ProductOfMallEntity

- 원인 : ProductOfMallEntity가 복합키 설정이 되어있지 않아 에러가 난다.

- 해결 : ProductOfMallEntity의 복합키용 class를(ProductMallId) 생성 하여 해결

#### 3.product-api 실행시 에러
- Error
  - org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [com.teamzero.product.ProductApplication]; nested exception is java.io.FileNotFoundException: class path resource [com/teamzero/product/config/GsonConfig.class] cannot be opened because it does not exist
    at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:188) ~[spring-context-5.3.24.jar:5.3.24]
    at org.springframework.context.annotation.Configu
- 원인 : config class 삭제 후 컴파일될 때 기존 BEAN으로 등록 된 config를 찾지 못해 발생되는 에러같다.
- 해결 : gradle에서 clean실행 후 정상해결