FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD canal-client-1.0.0.jar canal-client.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/canal-client.jar"]