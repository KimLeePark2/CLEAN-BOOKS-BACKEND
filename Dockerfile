# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim
# slim 버전
# FROM openjdk:17-jdk-slim

# Add Author info
LABEL maintainer="lsb123@g.yju.ac.kr"

# The application's jar file
ARG JAR_FILE=api/build/libs/api.jar

# Add the application's jar to the container
ADD ${JAR_FILE} clean-books-prod-server.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/clean-books-prod-server.jar", "--spring.profiles.active=prod"]
# Example
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/to-do-springboot.jar"]
# => java -Djava.security.edg=file:/dev/./urandom -jar /to-do-springboot.jar