## 自定义 springboot-starter

创建步骤见代码。

#### 在项目中引用
```xml
    <dependency>
        <groupId>com.liuyang1.springlearning.bootstarter</groupId>
        <artifactId>boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

然后在项目的 yml 配置或 properties 配置中给定配置值，
```yaml
com:
  liuyang1:
    mystarter:
      name: OneCodeMonkey
      version: v8

```