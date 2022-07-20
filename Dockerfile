# Build stage
FROM maven:3.6.0-jdk-11-slim AS MAVEN_BUILD

COPY src /home/app/src

COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package

#Run tomcat

FROM tomcat:jdk11-corretto

COPY --from=MAVEN_BUILD /home/app/target/notificator.war /usr/local/tomcat/webapps

EXPOSE 8080

CMD ["catalina.sh", "run"]