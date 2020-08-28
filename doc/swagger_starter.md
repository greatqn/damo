# swagger_starter

参考说明：https://gitee.com/didispace/spring-boot-starter-swagger

http://localhost:8080/swagger-ui.html

引用：
```
        <dependency>
            <groupId>com.damo</groupId>
            <artifactId>damo-swagger-boot-starter</artifactId>
            <version>${project.version}</version>
        </dependency>
```

配置：
```
swagger.enabled=true

swagger.title=\u7EDF\u4E00\u7F51\u5173\u63A5\u53E3
swagger.description=\u6CE8\u610F\u7F16\u7801\u89C4\u8303\u548C\u811A\u624B\u67B6
swagger.version=1.0
swagger.license=
swagger.licenseUrl=
swagger.termsOfServiceUrl= 
swagger.contact.name= 
swagger.contact.url= 
swagger.contact.email=22411414@qq.com
swagger.base-package=com.damo.api.controller
swagger.base-path=/**
swagger.exclude-path=/error, /ops/**
swagger.authorization.name=jwtToken
swagger.authorization.key-name=token-x-damo

swagger.globalOperationParameters[0].name=token-x-damo
swagger.globalOperationParameters[0].description=jwt token
swagger.globalOperationParameters[0].modelRef=string
swagger.globalOperationParameters[0].parameterType=header
swagger.globalOperationParameters[0].required=false

```

swagger.base-package=com.damo.api.controller 扫描的controller

中文乱码的话，需要转成\u格式。

globalOperationParameters 全局参数。

