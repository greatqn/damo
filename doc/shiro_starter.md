# shiro_starter

使用：
```
        <dependency>
            <groupId>com.damo</groupId>
            <artifactId>damo-shiro-boot-starter</artifactId>
            <version>${project.version}</version>
        </dependency>
```

jwt模式的配置：
```
shiro.web.mode=stateless
shiro.web.token-name=token-x-damo
shiro.web.secret=damosc

shiro.web.filter-chain-definition[0]=/api/v1/user/login=anon
shiro.web.filter-chain-definition[1]=/api/v1/user=authc,roles[T]
shiro.web.filter-chain-definition[2]=/api/v1/routes=authc,roles[G]
shiro.web.filter-chain-definition[3]=/api/v1/dashboard=authc,roles[G],perms[system:user:view]
shiro.web.filter-chain-definition[4]=/api/v1/weather/now.json=authc,perms[system:user:view]
shiro.web.filter-chain-definition[5]=/api/v1/**=authc
shiro.web.filter-chain-definition[6]=/**=anon
```

filter:

StatelessUserFilter == authc

shiro.web.token-name=token-x-damo ，header里的token名字。

shiro.web.secret=damosc 加密的盐。设成uuid的话，每次启动就新生成一个，老的token全部过期。

在设为authc的controller里，可以用 SecurityUtils.getSubject().getPrincipal() 取到token值。

获取payload:
Payload payload = jwtManager.verifyAndParsePayload(ShiroWebAutoConfiguration.DEFAULT_ISSUER, SecurityUtils.getSubject().getPrincipal().toString());




