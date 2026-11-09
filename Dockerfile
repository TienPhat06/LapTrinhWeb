# Stage 1: Build ứng dụng Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng trên Tomcat 10
FROM tomcat:10.1-jdk21-temurin

# Xóa sạch các app mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy chính xác file ROOT.war làm ứng dụng gốc
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]