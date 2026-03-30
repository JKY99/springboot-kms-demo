# Spring Boot KMS Demo

AWS KMS를 활용한 암호화/복호화 실습 프로젝트 (Java 8 + Spring Boot)

## 기술 스택

- Java 8
- Gradle
- Spring Boot 2.x
- AWS SDK for Java v1 (aws-java-sdk-kms)

---

## 실습 로드맵

### Step 0. 사전 준비

- [ ] AWS 계정 및 IAM 사용자 생성 (KMS 권한 부여)
- [ ] AWS CLI 설치 및 `aws configure` 설정
- [ ] AWS KMS에서 대칭 키(CMK) 생성 → Key ID/ARN 메모

```bash
aws kms create-key --description "demo-key"
aws kms create-alias --alias-name alias/demo-key --target-key-id <key-id>
```

---

### Step 1. 프로젝트 설정

`app/build.gradle`에 의존성 추가:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web:2.7.18'
    implementation 'com.amazonaws:aws-java-sdk-kms:1.12.772'
}
```

`application.yml` 설정:

```yaml
aws:
  kms:
    key-arn: arn:aws:kms:ap-northeast-2:<account-id>:alias/demo-key
  region: ap-northeast-2
```

---

### Step 2. KMS Client 빈 등록

```java
@Configuration
public class KmsConfig {
    @Bean
    public AWSKMS kmsClient() {
        return AWSKMSClientBuilder.standard()
            .withRegion("ap-northeast-2")
            .build();
    }
}
```

---

### Step 3. 암호화/복호화 서비스 구현

핵심 메서드 2개:

| 메서드 | 설명 |
|--------|------|
| `encrypt(String plaintext)` | KMS로 평문 암호화 → Base64 반환 |
| `decrypt(String ciphertext)` | Base64 입력 → KMS로 복호화 → 평문 반환 |

```java
// 암호화
EncryptRequest req = new EncryptRequest()
    .withKeyId(keyArn)
    .withPlaintext(ByteBuffer.wrap(plaintext.getBytes()));
ByteBuffer cipherBlob = kmsClient.encrypt(req).getCiphertextBlob();
return Base64.getEncoder().encodeToString(cipherBlob.array());

// 복호화
ByteBuffer cipherBlob = ByteBuffer.wrap(Base64.getDecoder().decode(ciphertext));
DecryptRequest req = new DecryptRequest().withCiphertextBlob(cipherBlob);
return new String(kmsClient.decrypt(req).getPlaintext().array());
```

---

### Step 4. REST API 노출

```
POST /kms/encrypt   body: { "plaintext": "hello" }
POST /kms/decrypt   body: { "ciphertext": "..." }
```

---

## 로컬 실행

```bash
./gradlew bootRun
```

## 디렉터리 구조 (목표)

```
app/src/main/java/org/example/
├── KmsConfig.java        # AWS KMS 클라이언트 빈
├── KmsService.java       # 암호화/복호화 로직
└── KmsController.java    # REST API
```
