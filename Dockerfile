FROM jenkins/jenkins:lts-alpine

USER root

RUN apk add --update docker && rm -rf /var/cache/apk/*

COPY init.groovy.d /usr/share/jenkins/ref/init.groovy.d/
COPY default-config /usr/share/jenkins/ref/

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
