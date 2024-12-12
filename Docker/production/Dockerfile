FROM registry.cn-shanghai.aliyuncs.com/hdbs/baseline:openjdk20slim

# 签名
MAINTAINER susir "3026610850@qq.com"

COPY ./hdbsthor-012-airflow.jar ./hdbsthor-012-airflow.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=GMT+08", "hdbsthor-012-airflow.jar", "--server.port=60012", "--spring.profiles.active=pro"]

EXPOSE 60012