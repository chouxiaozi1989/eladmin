FROM openjdk:21-rc
LABEL authors="caiyi"

ADD eladmin-system/target/eladmin-system-2.7.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]