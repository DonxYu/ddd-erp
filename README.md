#### 如何导入项目

- git clone 到本地
- 用 Idea -> File -> Open 不要用Import。打开ddd-erp 那个目录
- 进入每个独立项目找到pom.xml ，然后右键点击 ——> Add as Maven Project
- 等待下载包完成。这样就可以多模块项目开发了
- 如果用import 的话，需要定位到具体的项目，这时会打开多个Idea 窗口，
不太方便。

#### 创建一个数据库 db_erp_share 
- 数据库连接改为自己的账号和密码
- 数据库表将会自动创建


#### 在dev 环境下可以使用自动创建进行快速开发，模型推导

#### 在生产上务必自己创建Sql脚本，然后通过Flyway 管理

- flyway 配置项目中已经添加。
所有的脚本在resources -> erp 里面，项目启动会自动执行脚本

#### 如果没有配置 Eureka 项目会报错，这个不会影响使用

如果想不报错，启动一个Eureka 即可，更改配置项信息
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8000/eureka/