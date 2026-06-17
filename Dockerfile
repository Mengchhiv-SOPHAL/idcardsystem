FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y nginx openssh-server php-cli && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/idcardsystem-0.0.1-SNAPSHOT.jar app.jar
COPY nginx.conf /etc/nginx/sites-available/default

RUN mkdir -p /var/run/sshd

EXPOSE 8080 8443 22

CMD service ssh start && nginx && java -jar app.jar