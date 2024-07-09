#FROM openjdk:17-alpine
#WORKDIR /app
##UserWebService-0.0.1.war
#COPY . /app/.
#
##COPY /target/*.war UserWebService-0.0.1.war
#
#RUN mvn -f /app/pom.xml clear package
#EXPOSE 8080
#ENTRYPOINT["java", "-jar", "/app/*.war"]

FROM openjdk:17-alpine
WORKDIR /app
COPY /target/UsersWebService-0.0.1-SNAPSHOT.war /app/UserWebService-0.0.1.war
ENTRYPOINT ["java", "-jar", "/app/UserWebService-0.0.1.war"]