FROM openjdk:8-jdk-stretch
MAINTAINER xuejunze 646980564@qq.com
COPY /root/app.jar   /user
ENTRYPOINT ["java","-jar","app.jar"]

