Maven 插件

damo-maven-plugin

<plugin>
    <groupId>com.damo</groupId>
    <artifactId>damo-maven-plugin</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</plugin>
        	
功能一：建子项目

建目录 damo-service-test
copy pom.xml
修改artifactId为damo-service-test
运行：mvn damo:project
自动生成
<module>damo-service-test-api</module>
<module>damo-service-test-consumer</module>
<module>damo-service-test-provider</module>


建boot子项目
运行：mvn damo:project_boot

功能二：表结构转实体类

mvn damo:mysql -Dprefix=sp_ -Dfile=C:\Users\Administrator\Desktop\db.sql -Dpackage=com.damo.api.service.word -Ddbname=damo

file=db.sql 为表结构的DDL

package 包名

dbname=wrong_topic 库名

prefix=wt_ 表前缀


功能三：添加一个接口

mvn damo:newapi -Daction=user_create -Dpackage=com.damo.user -Dlevel=service

功能四：表结构转mongo类

mvn damo:mongo -Dprefix=wt_ -Dfile=C:\Users\Administrator\Desktop\db.sql -Dpackage=com.damo.wrongtop 

file=db.sql 为表结构的DDL

package 包名

prefix=wt_ 表前缀


功能五：生成service，boot版

mvn damo:boot_service -Dpackage=com.damo.service.user  -Dname=UserService
