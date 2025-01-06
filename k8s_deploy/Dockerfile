# FROM swr.cn-east-2.myhuaweicloud.com/common-server/alpine-java-17-fonts:3.0 (our image addr)
FROM swr.cn-east-2.myhuaweicloud.com/common-server/alpine-java-17-fonts:3.1
COPY app.jar /opt/
WORKDIR /opt/
CMD crond && java -XX:InitialRAMPercentage=75.0 -XX:MaxRAMPercentage=75.0 -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -XX:+UseAdaptiveSizePolicy -XX:+HeapDumpOnOutOfMemoryError -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 /opt/app.jar 
