FROM maven:3.8.5-openjdk-17-slim as tms_backend
WORKDIR /build/
COPY pom.xml ./
COPY src ./src/
RUN mvn package -DskipTests
RUN mkdir -p /opt/app/
RUN cp $(find ./target/ -name '*.jar') /opt/app/TMS-backend.jar
ENTRYPOINT ["java", "-jar", "/opt/app/TMS-backend.jar"]