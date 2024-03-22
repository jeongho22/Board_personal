

FROM amazoncorretto:17
# 프로젝트 루트에서 JAR 파일의 상대 경로 지정
COPY build/libs/dy-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]


