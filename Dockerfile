FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/houseme-1.0-SNAPSHOT.jar app.jar
#RUN apk add --update bash && rm -rf /var/cache/apk/*
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
