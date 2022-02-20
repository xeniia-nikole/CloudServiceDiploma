FROM openjdk:15
ADD build/libs/DiplomGradle-1.0-SNAPSHOT.jar diplom.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/diplom.jar"]