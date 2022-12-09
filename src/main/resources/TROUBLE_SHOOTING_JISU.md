# Trouble Shooting Record

#### 1. CATEGORY-ADD Barnch
- Error
  - Reqeust Parameter값에 널 값이 들어올때 널 확인 부분오류

- 원인 : isEmpty()로 NUll값 체크
 ```java
    if(parameter.temp.isEmpty()){
      return "null";
    }
 ```
(2) 해결 : isEmpty()는 값의 길이를 체크하여 길이가 0이면 true 아니면 false를 리턴하는 메소드이다.
값자체가 NULL이면 길이를 가져올수 없으므로 에러가 난다. 
 ```java
  if(Objects.isNull((parameter.temp)){
    return true;
    }
 ```
Objects.isNull()이용하여 널체크 하였다.