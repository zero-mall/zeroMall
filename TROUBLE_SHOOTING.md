# Trouble Shooting Record

## (각자 트러블 슈팅 내역을 아래에 적어주세요) <br> <br>          

### 고은
#### 1. @WebMvcTest 중 에러 발생 - JPA metamodel must not be empty!
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

#### 2. JPA Repository가 정상적으로 DI 되지 않음
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

### 찬혁

### 지수