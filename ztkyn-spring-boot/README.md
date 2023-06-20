[ztkyn-boot]是采用SpringBoot3.0、SpringSecurity6.0、Mybatis-Plus等框架，开发的一套企业级脚手架系统，使用门槛极低，且采用MIT开源协议，完全免费开源，可免费用于商业项目等场景。

### 项目特点

-   采用组件模式，通过组件扩展业务功能，系统再也不臃肿
-   友好的代码结构及注释，便于阅读及二次开发
-   采用前后端分离架构，更符合企业开发需求
-   完善的权限控制，可控制到页面或按钮
-   完善的数据权限，可方便实现数据隔离
-   完善的XSS防范及脚本过滤，彻底杜绝XSS攻击
-   支持多家存储服务，如：本地上传、Minio、阿里云、腾讯云、七牛云、华为云等
-   支持多家短信发送服务，如：阿里云、腾讯云、七牛云、华为云等
-   支持swagger文档，方便编写API接口文档

### 目录结构

```
ztkyn-boot
├─db        数据库SQL
│  ├─dm8    达梦
│  └─mysql  MySQL
│ 
├─ztkyn-boot-api           API模块（用于各模块解耦）
│ 
├─ztkyn-boot-module
│    ├─ztkyn-module-quartz   定时任务
│    └─ztkyn-module-message  短信模块
│  
├─ztkyn-boot-new           新业务模块（用于开发新业务，不使用可删除）
│ 
├─ztkyn-boot-system        系统模块（用户、角色、部门、菜单等系统功能）
│  
├─ztkyn-framework          框架模块
│ 
├─ztkyn-server             服务模块（负责项目的配置、启动）
│ 
├─pom.xml                 maven依赖管理
```