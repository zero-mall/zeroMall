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
 (2) 해결 : (방법1이 잘 되지 않아, 방법2를 사용했다.)
 - 방법1 : JpaAuditing Config에 @EnableJpaAuditing을 따로 걸어두거나
 - 방법2 : WebMvc 테스트 클래스에 @MockBean(JpaMetamodelMappingContext.class)를 추가한다.

### 찬혁

### 지수