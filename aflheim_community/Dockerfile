FROM oracle/openjdk:17.0.10
WORKDIR /app
COPY aflheim_community/target/AlfheimCommunity.jar /app/AlfheimCommunity.jar
EXPOSE 8080
CMD ["java", "-jar", "AlfheimCommunity.jar"]