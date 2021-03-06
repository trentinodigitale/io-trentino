# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine
RUN echo "Build image IO-ITALIA"
CMD echo "Middleware IO Trentino"


# Add Maintainer Info
LABEL maintainer="mirko.pianetti@tndigit.it"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/io-trentino-0.6.0.jar

# Add the application's jar to the container
ADD ${JAR_FILE} io-trentino.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/io-trentino.jar"]
