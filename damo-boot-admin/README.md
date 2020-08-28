boot-admin

编译：mvn install -DskipTests

运行：java -jar target/damo-boot-admin-2.0.0.jar

boot应用的监控中心。

访问： http://127.0.0.1:8081/


客户端：
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

#接入点管理
management.endpoints.web.exposure.include=* 
#management.endpoints.web.exposure.exclude=env
management.endpoint.health.show-details=always

#boot admin 配置 http://codecentric.github.io/spring-boot-admin/2.0.1/
spring.boot.admin.client.name = damo-service
spring.boot.admin.client.instance.service-url=http://127.0.0.1:8080/


[boot项目监控](../doc/boot_admin.md)