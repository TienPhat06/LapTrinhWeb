# Stage 1: Build file WAR bằng Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng trên Tomcat 10
FROM tomcat:10.1-jdk21-temurin

# Xóa ứng dụng mặc định và tạo thư mục ROOT mới
RUN rm -rf /usr/local/tomcat/webapps/* && mkdir -p /usr/local/tomcat/webapps/ROOT

# Cài unzip và giải nén trực tiếp file WAR vào webapps/ROOT
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/target/ROOT.war /tmp/app.war
RUN unzip /tmp/app.war -d /usr/local/tomcat/webapps/ROOT/ && rm /tmp/app.war

EXPOSE 8080
CMD ["catalina.sh", "run"]