# 개요
- AI Adapter에서 스트리밍 응답을 받기 위한 UI 및 벡엔드(java) 샘플 코드입니다.
- 기본적인 사용방법에 대한 샘플만 작성하고 있으니 각 제품 별로 적절하게 변환해서 사용해야 합니다.

# 코드 설명
## Backend
SSE를 사용하기 위해 2가지 예제로 작성되어 있습니다.
1. HttpClient 사용
2. WebFlux 사용

코드는 `sse-client-server`에 있습니다.

기본 예제는 Spring Boot 2.7.16으로 작성되어 있으며 적용 시 필요한 소스파일만 각 제품에 적용하시면 됩니다.

### 1. HttpClient 사용
EER용으로 사용하기 위해 작성되어 있으며 Java 1.6 이상에서 사용 가능합니다.
`HttpClientSseController.java`를 참고하시면 되고 추가적인 예외처리는 필요 시 추가해야할 수도 있습니다.
비동기 지원이 필요하면 HttpAsyncClient를 사용하시면 됩니다.

#### 의존성 추가
```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
</dependency>
```

### 2. WebFlux 사용
CSTalk용으로 사용하기 위해 작성되어 있으며 Java 1.8에서 사용 가능합니다.
CSTalk에서도 WebFlux 대신 HttpClient를 사용해도 무방합니다.
코드는 샘플코드이므로 제품코드에 맞게 수정되어야 할 수 있습니다.

#### 의존성 추가
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

## Frontend
SSE를 사용하기 위한 간단한 UI 화면 및 streaming 응답처리 내용이 포함되어 있습니다.

Frontend 코드는 `EER`에서 사용하기 위한 `jQuery` 기반으로 작성되어 있습니다. 만일 React 코드가 필요하면 필요한 부분만 가지고 수정하시면 됩니다.

소스는 `sse-client-ui`에 있고 sse를 가져와서 응답처리를 하는 코드는 `fetch-sse.js` 내에 있습니다.


### Front UI 서버 실행방법
데모의 경우 `vite`를 통해 웹서버를 기동하고 있으며 데모를 실행하기 위해서는 `node`가 설치되어 있어야 합니다.

```bash
yarn

yarn dev

# http://localhost:5173 으로 접속
```

index.html 내에 답변 요약과 답변 추천에 대한 기능이 포함되어 있습니다.
`hostUrl`은 EER 혹은 CSTalk에 맞게 수정하시면 됩니다.

httpClient를 사용하는 경우
```js
var hostUrl = 'http://172.16.100.225:8080/httpClient/sseClient'
```
WebFlux를 사용하는 경우
```js
var hostUrl = 'http://172.16.100.225:8080/webflux/sseClient'
```

body에 포함되는 url은 aiadapter의 API 주소이며, 샘플 예제로 보편적으로 사용할 용도로 만든 것이라 제품 적용시에는 Backend에서 aiadapter 주소를 매핑하셔도 됩니다.

예)
```js
var body = {
  "url": "http://172.16.100.225:8181/streaming/DEFAULT/summarize", // AI 서버 경로
  "payload": { // AI 서버에 전달할 데이터
    "sentence": sentence,
    "metadata": metadata
  }
}
```
