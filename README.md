# Spring IoC Container v3

## 프로젝트 소개

Spring의 핵심 개념인 IoC와 DI를 직접 구현한 프로젝트입니다.

v3에서는 v2의 컴포넌트 스캔 기반 Bean 생성에 더해,  
`@Configuration` 클래스 내부의 `@Bean` 메서드를 실행하여 Bean을 등록하는 기능을 추가했습니다.

## 구현 기능

- Reflections 기반 컴포넌트 스캔
- `@Component`, `@Service`, `@Repository`, `@Configuration` 스캔
- 생성자 기반 DI
- Singleton Bean 관리
- `@Bean` 메서드 기반 Bean 등록
- `@Bean` 메서드 파라미터 기반 의존성 주입

## 테스트 항목

- ApplicationContext 객체 생성
- testPostService Bean 생성
- Singleton 검증
- testPostRepository Bean 생성
- testPostService 의존성 주입
- testFacadePostService 의존성 주입
- `@Bean` 기반 testBaseJavaTimeModule 생성
- `@Bean` 기반 testBaseObjectMapper 생성

## 실행 방법

```bash
./gradlew test