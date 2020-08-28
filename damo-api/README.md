统一网关

编译：mvn install -DskipTests

运行：java -jar target/damo-api-1.0.0-SNAPSHOT.jar

swagger: http://localhost:8080/swagger-ui.html#/

业务代码：

仅在controller目录添加代码。直接桥接dubbo里的服务对象。

只做字段校验，不做任何业务逻辑。