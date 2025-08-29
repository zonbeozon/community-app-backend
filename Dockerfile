#FROM eclipse-temurin:21-jdk-jammy as builder
#WORKDIR /app
#COPY . .
#RUN ./gradlew build -x test
#
#FROM eclipse-temurin:21-jre-jammy
#WORKDIR /app
#COPY --from=builder /app/build/libs/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
# 1. 빌더 스테이지: 애플리케이션 빌드
FROM eclipse-temurin:21-jdk-jammy AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 2. Gradle Wrapper 관련 파일 복사 (자주 변경되지 않는 부분)
#    Wrapper 스크립트와 Gradle 설정 디렉토리를 먼저 복사하여 캐시 활용도를 높입니다.
COPY gradlew .
COPY gradle gradle/

# 3. Gradle 빌드 스크립트 및 설정 파일 복사 (의존성 정의, 소스코드보다 변경 빈도 낮음)
#    build.gradle, settings.gradle 파일을 먼저 복사하여
#    의존성 다운로드 레이어가 캐시될 수 있도록 합니다.
COPY build.gradle .
COPY settings.gradle .
# 만약 멀티 모듈 프로젝트라면, 모든 서브모듈의 build.gradle도 여기에 COPY 합니다.
# 예: COPY sub-project-name/build.gradle sub-project-name/

# 4. 의존성만 먼저 다운로드 (이 레이어가 캐시될 가능성이 가장 높습니다)
#    소스 코드가 변경되어도 이 단계는 다시 실행되지 않도록 캐시를 유지합니다.
#    --no-daemon: 컨테이너 빌드에서는 Gradle Daemon 사용 안 함 (오버헤드 방지).
#    -x test: 테스트는 빌드 시간 단축을 위해 이 단계에서 스킵합니다.
#    --build-cache: 빌드 내부 캐시를 활성화하여 효율을 높입니다 (Docker Buildx와 시너지).
#    || true: 의존성 다운로드 자체가 실패하더라도 (예: 네트워크 문제) 빌드 실패로 간주하지 않고 진행 (진단 용이).
RUN ./gradlew dependencies --no-daemon -x test --build-cache || true

# 5. 소스 코드 복사 (가장 자주 변경되는 부분)
#    의존성 다운로드 후에 소스 코드를 복사하여, 소스 코드 변경 시에도
#    이전 의존성 다운로드 레이어를 재사용할 수 있도록 합니다.
COPY src src/

# 6. 실제 애플리케이션 빌드 실행
#    의존성은 이미 다운로드되었으므로 이 단계는 컴파일 및 패키징에만 집중합니다.
#    -x test: 다시 한번 테스트를 스킵하여 빌드 시간을 단축합니다.
RUN ./gradlew build --no-daemon -x test --build-cache

# 7. 런타임 스테이지: 가벼운 JRE 이미지로 최종 애플리케이션 실행 환경 구성
FROM eclipse-temurin:21-jre-jammy

# 8. 작업 디렉토리 설정
WORKDIR /app

# 9. 빌더 스테이지에서 생성된 JAR 파일 복사
#    Gradle 빌드 결과물 경로에 따라 정확히 지정해야 합니다.
#    일반적으로 build/libs/*.jar에 생성됩니다.
COPY --from=builder /app/build/libs/*.jar app.jar

# 10. 애플리케이션이 사용할 포트 노출
EXPOSE 8080

# 11. 애플리케이션 시작 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]