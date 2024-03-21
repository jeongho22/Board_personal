FROM openjdk:17

# 프로젝트 루트에서 JAR 파일의 상대 경로 지정
ARG JAR_FILE=build/libs/dy-0.0.1-SNAPSHOT.jar

# JAR 파일을 컨테이너 내 app.jar로 복사
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
