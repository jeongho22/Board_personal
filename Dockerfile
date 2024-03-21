



FROM openjdk:17

# JAR_FILE 변수에 빌드 디렉토리의 상대 경로를 포함하여 JAR 파일 지정
ARG JAR_FILE=build/libs/*.jar

# COPY 명령어에서 JAR_FILE 변수를 사용하여 해당 JAR 파일을 app.jar로 복사
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
