FROM openjdk
EXPOSE 8080
ADD target/bingo.jar bingo.jar
ENTRYPOINT ["java", "-jar", "/bingo.jar"]