FROM frolvlad/alpine-oraclejdk8
VOLUME /tmp
ADD canal-server-1.0.0.jar canal-server.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/canal-server.jar"]