# Stage 1: Build ứng dụng với Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Tải dependencies trước để tối ưu build cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy toàn bộ code và đóng gói WAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy trên Tomcat 10 (Jakarta EE 10)
FROM tomcat:10.1-jdk21-temurin

# Xóa ứng dụng mặc định của Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy đúng 1 file WAR đầu ra và đổi tên thành ROOT.war
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]