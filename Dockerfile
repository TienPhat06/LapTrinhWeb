# Stage 1: Build ứng dụng Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng trên Tomcat 10
FROM tomcat:10.1-jdk21-temurin

# Xóa ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy đúng 1 file ROOT.war vào thư mục webapps để Tomcat làm trang gốc (/)
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]