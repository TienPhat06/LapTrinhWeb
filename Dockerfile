# Stage 1: Build ứng dụng bằng Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Sao chép file cấu hình và source code
COPY pom.xml .
COPY src ./src

# Đóng gói file ROOT.war
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng trên Tomcat 10
FROM tomcat:10.1-jdk21-temurin

# Xóa toàn bộ ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Sao chép đúng file ROOT.war đã được Maven build vào làm trang gốc
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]