# 1. Build ứng dụng với Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Chạy ứng dụng trên Tomcat 10 (hỗ trợ Jakarta EE 10 / Servlet 6.0)
FROM tomcat:10.1-jdk21-temurin
# Xóa sạch tất cả app mặc định của Tomcat để tránh xung đột
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy file .war mới build vào và đổi tên thành ROOT.war để làm trang chủ
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]