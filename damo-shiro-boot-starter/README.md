参考
https://github.com/taccisum/shiro-starter
https://shiro.apache.org/spring-boot.html

# 版本信息

 - spring-boot: `2.0.6.RELEASE`
 - shiro-spring: `1.4.0-RC2`
 - java-jwt:`3.4.0`


1. 创建JWT

```
@Autowired
private JWTManager jwtManager;

public String login() {
    Payload payload = new Payload();
    payload.put("uid", 123L);
    payload.put("username", "tac");
    payload.put("roles", "STAFF,DEVELOPER");
    payload.put("permissions", "system:user:view,system:user:add");
    return jwtManager.create("access_token", payload);
}
```

2. 解析token为payload

```
@Autowired
private JWTManager jwtManager;

public Payload parseJWT() {
    return jwtManager.verifyAndParsePayload("access_token", SecurityUtils.getSubject().getPrincipal().toString());
}
```

DEFAULT_ISSUER=access_token 先用默认发行者。固定payload格式。ShiroWebAutoConfiguration里修改。

## 配置一览
|properties|描述|默认值|适用模式|
|:--|:-|:-|:-|
|shiro.web.mode|指定shiro启动模式|SESSION,STATELESS|ALL|
|shiro.web.redirect-enabled|是否允许shiro重定向页面|true|ALL|
|shiro.web.filter-chain-definition|定义shiro filter chain|{}|ALL|
|shiro.loginUrl|用户未认证时重定向的页面|/login.jsp|ALL|
|shiro.successUrl|用户认证成功后跳转的默认页面|/|ALL|
|shiro.unauthorizedUrl|用户未授权时的重定向页面(403)|null|ALL|
|shiro.web.token-name|token字段名,在head或post里|token|STATELESS|
|shiro.web.secret|密码盐，uuid时会每次动态生成|uuid|STATELESS|
|shiro.web.filter-chain-definition[0]|匹配器，从上到下，从左到右|/**=anon|STATELESS|


拦截器链：https://www.cnblogs.com/litblank/p/7883167.html

```
  "token": {
    "uid": 123,
    "permissions": "system:user:view,system:user:add",
    "roles": "S,T",
    "username": "tac"
  }

shiro.web.filter-chain-definition[0]=/api/v1/user/login=anon true
shiro.web.filter-chain-definition[1]=/api/v1/user=authc,roles[T] true
shiro.web.filter-chain-definition[2]=/api/v1/routes=authc,roles[G] false
shiro.web.filter-chain-definition[3]=/api/v1/dashboard=authc,roles[G],perms[system:user:view] false
shiro.web.filter-chain-definition[4]=/api/v1/weather/now.json=authc,perms[system:user:view] true
shiro.web.filter-chain-definition[5]=/api/v1/**=authc true
shiro.web.filter-chain-definition[6]=/**=anon true
```

横的一行，是AND的关系，有一个false，就被拦截。

