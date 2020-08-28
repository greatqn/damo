# damo
一个脚手架，基于jdk1.8,spring-boot2.3.3,dubbo2.7.7。用于快速启动项目，进入业务开发。

## 关于作者

  十多年技术团队管理经验（20人以内），丰富的高并发、高可用系统架构经验（能抄会用）。对技术保持新鲜，保持饥渴。
  
### 组织结构

``` lua
damo
├── damo-parent -- 引用定义[v2.1]
├── damo-api -- 统一网关[v2.0]
├── damo-api-test -- 统一网关的集成测试[v2.0]
├── damo-service -- dubbo服务[v2.1]
|    ├── damo-service-api -- 服务定义[v2.1]
|    ├── damo-service-consumer -- 服务调用者[v2.1]
|    └── damo-service-provider -- 服务提供者[v2.1]
├── damo-service-sn -- dubbo序号服务[v2.1]
├── damo-config-apollo -- apollo配置中心[v1.1]
|    ├── apollo-adminservice -- 管理服务[v1.1]
|    ├── apollo-configservice -- 配置服务[v1.1]
|    └── apollo-portal -- 控制台[v1.1]
├── damo-nacos -- nacos配置中心,注册中心[v1.2]
├── damo-boot-admin -- boot监控[v2.0]
├── damo-shiro-boot-starter -- shiro,jwt插件[v2.0]
├── damo-swagger-boot-starter -- swagger插件[v2.0]
```

### 技术选型

技术 | 名称 | 官网
----|------|----
Apache Shiro | 安全框架  | [http://shiro.apache.org/](http://shiro.apache.org/)
Druid | 数据库连接池  | [https://github.com/alibaba/druid](https://github.com/alibaba/druid)
Dubbo | 分布式服务框架  | [http://dubbo.io/](http://dubbo.io/)
Redis | 分布式缓存数据库  | [https://redis.io/](https://redis.io/)
RocketMq | 消息队列 | [https://rocketmq.apache.org/](https://rocketmq.apache.org/)
Apollo | 配置中心  | [https://github.com/ctripcorp/apollo/](https://github.com/ctripcorp/apollo/)
Nacos | 动态服务发现、配置管理和服务管理平台  | [https://nacos.io/zh-cn/](https://nacos.io/zh-cn/)
Soul | API网关 | [https://gitee.com/shuaiqiyu/soul](https://gitee.com/shuaiqiyu/soul)
Swagger2 | 接口测试框架  | [http://swagger.io/](http://swagger.io/)
Jenkins | 持续集成工具  | [https://jenkins.io/index.html](https://jenkins.io/index.html)
Maven | 项目构建管理  | [http://maven.apache.org/](http://maven.apache.org/)

项目编译 运行install.bat

mvn clean install -DskipTests

加我微信，备注damo，交个朋友：

![greatqn](https://github.com/greatqn/damo/raw/master/doc/greatqn.png)

### 使用说明

[项目启动说明](./doc/dubbo_start.md)

[dubbo的性能测试](./doc/dubbo_test.md)

[boot项目监控](./doc/boot_admin.md)

dubbo 2.7.7 的新配置中心

dubbo 监控

dubbo admin

soul网关

数据库版本管理

[序号服务](./doc/sn_starter.md)
