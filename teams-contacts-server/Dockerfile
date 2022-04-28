FROM adoptopenjdk:11-jre-hotspot as builder
USER root
RUN mkdir -p /contacts
RUN addgroup --system contacts && adduser --system --ingroup contacts contacts
RUN chown contacts:contacts /contacts
USER contacts:contacts
WORKDIR /contacts
ARG JAR_FILE=target/*exec.jar
COPY ${JAR_FILE} contacts-backend.jar
RUN java -Djarmode=layertools -jar contacts-backend.jar extract

FROM adoptopenjdk:11-jre-hotspot
USER root
RUN mkdir -p /contacts
RUN addgroup --system contacts && adduser --system --ingroup contacts contacts
RUN chown contacts:contacts /contacts
USER contacts:contacts
WORKDIR /contacts
COPY --from=builder /contacts/dependencies/ ./
COPY --from=builder /contacts/snapshot-dependencies/ ./
COPY --from=builder /contacts/spring-boot-loader/ ./
COPY --from=builder /contacts/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]