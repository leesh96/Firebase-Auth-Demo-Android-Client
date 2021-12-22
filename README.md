# Firebase-Auth-Demo-Android-Client

Firebase Auth를 사용하여 자체 백엔드 서버와 인증, 인가를 구현한 데모의 안드로이드 클라이언트 앱 입니다.

## Important

- 인증(authentication) : 유저가 누군지 확인하는 절차
- 인가(authorization) : 유저에 대한 권한을 허가하는 것

기본적으로 API 호출에는 인증과 인가를 요구해야 한다.

예를 들어, 깃허브의 리포지토리를 검색하는 API는 사용자가 누군지 몰라도 되고 로그인 없이도 할 수 있다. -> 인증 필요 X

하지만 커밋, 푸쉬, PR, 개인정보 변경 등의 작업은 사용자가 누군지 알아야된다. -> 인증, 인가 필요

인증을 하는 방법에는 세션, 쿠키, JWT(Json Web Token) 방식이 있지만 모바일에서는 Token 방식을 주로 사용

API 요청 헤더에 발급받은 JWT를 넣어 요청을 보낸다.

## Structure

회원가입, 비밀번호 암호화, 사용자 관리 등의 복잡하고 어려운 작업을 Firebase Auth와 그 안의 Google Login을 사용하여 구현

1. 사용자가 로그인하려 함 (사용자 -> 클라이언트)
2. 구글 계정에 대한 인증 화면 (클라이언트 -> 사용자)
3. 인증 (사용자 -> 클라이언트)
4. 인증 정보(구글 idToken) 전송 (클라이언트 -> Firebase Auth)
5. 인증 후 토큰 발급(파이어베잇 idToken, 1시간 유효) (Firebase Auth -> 클라이언트)
6. 클라이언트가 토큰을 로컬에 저장하고 있는다. 안드로이드의 경우 SharedPreference나 DataStore 사용
7. 자체 백엔드 서버에 파이어베이스 토큰을 사용하여 인증 (클라이언트 -> 자체 백엔드)
8. 자체 백엔드 서버에서 파이어베이스 토큰 검증 (자체 백엔드 -> Firebase Auth)
9. 토큰이 유효하면 해당 토큰의 사용자 정보로 요청 처리, 토큰이 유효하지 않으면 401 UNAUTORIZED 응답 (자체 백엔드 -> 클라이언트)
10. 응답 처리 (클라이언트)

해당 예제에서는 파이어베이스 idToken을 SharedPreference에 저장하고 API 요청 시 OkHttp에 Interceptor를 구현하여 요청 헤더에 Autorization 키에 "Bearer ${firebase idToken}" 형태로 담아서 요청 전송

만약, 401 코드를 응답 받으며 idToken이 만료되었을 가능성이 높으므로 재발급을 Interceptor에서 처리

Interceptor는 HTTP 요청도 중간에서 보내기 전에 처리가능하고 응답도 처리가능하다.

여러 개의 Interceptor, OkHttpClient, Retrofit을 잘 사용하여 상황에 맞게 요청 보낼 수 있음 -> DI를 사용하면 효과적임



