# Spring Security JWT 인증 프로젝트

## 🌐 배포 URL
- **API 기본 URL**: http://ec2-13-125-227-244.ap-northeast-2.compute.amazonaws.com:8080](http://ec2-13-125-227-244.ap-northeast-2.compute.amazonaws.com:8080
- **Swagger UI**: http://ec2-13-125-227-244.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html](http://ec2-13-125-227-244.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui

---

## 🧪 Access / Refresh Token 테스트 시나리오

### 1. Access Token 생성
- **목적:**  
  Access Token 생성 기능이 올바르게 동작하는지 확인합니다.
- **테스트 절차:**
    1. Access Token을 생성합니다.
    2. 생성된 토큰이 `null`이 아닌지 확인합니다.
    3. 토큰이 `Bearer` 접두어로 시작하는지 확인합니다.
- **기대 결과:**  
  유효한 Access Token이 반환됩니다.

---

### 2. Refresh Token 생성
- **목적:**  
  Refresh Token 생성 기능이 올바르게 동작하는지 확인합니다.
- **테스트 절차:**
    1. Refresh Token을 생성합니다.
    2. 생성된 토큰이 `null`이 아닌지 확인합니다.
    3. 토큰이 `Bearer` 접두어로 시작하는지 확인합니다.
- **기대 결과:**  
  유효한 Refresh Token이 반환됩니다.

---

### 3. Claims 추출
- **목적:**  
  Access Token에서 `Claims` 정보를 올바르게 추출할 수 있는지 확인합니다.
- **테스트 절차:**
    1. Access Token을 생성합니다.
    2. `Bearer` 접두어를 제거한 후, 토큰에서 `Claims`를 추출합니다.
    3. 추출된 `Claims`의 `subject`와 `username` 값이 올바른지 확인합니다.
- **기대 결과:**
    - `subject`는 사용자 ID와 일치해야 합니다.
    - `username`은 토큰 생성 시 설정한 값과 일치해야 합니다.

---

### 4. 토큰 유효성 확인
- **목적:**  
  Access Token 유효성 검사 기능이 정상적으로 동작하는지 확인합니다.
- **테스트 절차:**
    1. 유효한 Access Token을 생성합니다.
    2. 토큰 유효성을 검사합니다.
- **기대 결과:**  
  토큰은 항상 유효해야 합니다.

---

### 5. 잘못된 서명 토큰 처리
- **목적:**  
  잘못된 서명을 가진 토큰 처리 시, 적절한 예외가 발생하는지 확인합니다.
- **테스트 절차:**
    1. 유효한 Access Token을 생성한 후, 서명 부분을 변조하여 잘못된 서명 토큰을 만듭니다.
    2. 변조된 토큰에서 `Claims`를 추출하려고 시도합니다.
- **기대 결과:**  
  `SignatureException`이 발생합니다.

---

### 6. 만료된 토큰 처리
- **목적:**  
  만료된 토큰을 처리할 때, 적절한 예외가 발생하는지 확인합니다.
- **테스트 절차:**
    1. 짧은 만료 시간을 가진 Access Token을 생성합니다.
    2. 토큰이 만료될 때까지 대기한 후, `Claims`를 추출하려고 시도합니다.
- **기대 결과:**  
  `ExpiredJwtException`이 발생합니다.

---

### 7. Bearer 접두어 제거
- **목적:**  
  토큰 문자열에서 `Bearer` 접두어를 올바르게 제거할 수 있는지 확인합니다.
- **테스트 절차:**
    1. `Bearer` 접두어가 포함된 토큰 문자열을 제공하고, 접두어를 제거합니다.
- **기대 결과:**  
  접두어가 제거된 순수한 토큰 문자열이 반환됩니다.

---

### 8. 잘못된 토큰 접두어 처리
- **목적:**  
  `Bearer` 접두어가 없는 잘못된 토큰 문자열을 처리할 때, 적절한 예외가 발생하는지 확인합니다.
- **테스트 절차:**
    1. `Bearer` 접두어가 없는 잘못된 토큰 문자열을 제공한 후, 접두어 제거 메서드를 호출합니다.
- **기대 결과:**  
  `ServerException`이 발생합니다.

---


