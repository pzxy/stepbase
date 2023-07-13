# topbase

1. topbase-core
    - topbase-core-ref 基础POJO类、常量、枚举、转写器
    - topbase-core-framework
    - topbase-core-mvc
    - topbase-core-common
    - topbase-core-aop AOP公共类
    - topbase-core-devtools 开发工具包
2. topbase-component
    - topbase-component-aliyun-oss 阿里云OSS、短信
    - topbase-component-aws-s3 Aws s3
    - topbase-component-boot-swagger Swagger
    - topbase-component-email
    - topbase-component-upload 文件上传基础类实现包为oss、s3
    - topbase-component-xxljob xxljob
3. topbase-data
   - topbase-data-mongo
4. topbase-service
    - topbase-service-admin 权限管理
    - topbase-service-base 基础服务
    - topbase-service-logger 操作日志
    - topbase-service-utils
    - topbase-service-workflow 工作流
5. topbase-blockchain
    - topbase-blockchain-wallet
    - topbase-blockchain-bitcoin
    - topbase-blockchain-ethereum
    - topbase-blockchain-filecoin
   
#### maven
```
mvn clean install -Dmaven.test.skip=true
```