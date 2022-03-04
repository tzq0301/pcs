# 心理咨询系统（Psychological Counseling System）

## 项目简介

心理咨询系统是辅助线下心理咨询的线上平台。

学生可以在心理咨询系统中填写登记表，进行线上初访预约，并由管理员进行审核；审核通过后，初访员会对学生进行初访，了解大致情况；此后，咨询助理安排咨询师对学生进行心理咨询，由咨询师提交每次访问的记录，并在完成全部心理咨询后填写结案报告。

需求来源：http://wiki.suncaper.net/pages/viewpage.action?pageId=50369779

## 业务流程

![业务流程图](https://tzq-oos-1.oss-cn-hangzhou.aliyuncs.com/img/image-20220227103201232.png)

## 业务功能

![心理资讯系统 v3.1](https://tzq-oos-1.oss-cn-hangzhou.aliyuncs.com/img/%E5%BF%83%E7%90%86%E8%B5%84%E8%AE%AF%E7%B3%BB%E7%BB%9F%20v3.1.png)

## 数据模型

![数据模型 v2.1](https://tzq-oos-1.oss-cn-hangzhou.aliyuncs.com/img/%E6%95%B0%E6%8D%AE%E6%A8%A1%E5%9E%8B%20v2.1.jpg)

## 技术选型

| 技术        | 选型                                               | 备注                                                   |
|-----------|--------------------------------------------------|------------------------------------------------------|
| 微服务注册中心   | Spring Cloud Alibaba Nacos                       ||
| 微服务配置管理中心 | Spring Cloud Alibaba Nacos                       ||
| 微服务网关     | Spring Cloud Gateway                             ||
| 负载均衡      | Spring Cloud Loadbalancer (ReactiveLoadBalancer) | 不实用 Ribbon 提供的阻塞式负载均衡，使用响应式负载均衡 ReactiveLoadBalancer |
| HTTP 客户端  | WebClient <br/> ~~(Spring Cloud OpenFeign)~~     ||
| 认证授权      | Spring Security + JWT                            |                                                      |
| Web 服务    | Spring WebFlux                                   |                                                      |
| 数据存储      | MongoDB                                          | 阿里云数据库（三节点副本集实例，价值 ¥9.99）                            |
| 云服务器      | 阿里云轻量应用服务器、腾讯云轻量应用服务器                            |                                                      |
| 短信发送      | 腾讯云 SMS                                          |                                                      |
| 缓存        | Redis Cluster ( 1 Master & 2 Salves )            | 基于 Docker Compose 的"一主二从"三节点 Redis 集群                |
| 消息队列      | RabbitMQ                                         | 阿里云 AMQP (RabbitMQ)                                  |

## 技术架构

## 微服务管理

### 微服务通信

```mermaid
flowchart LR
    client[Client] --> |Web Request| gateway[Gateway]
    
    subgraph java [Spring Cloud Microservices]
        gateway --> rabbit[Rabbit MQ]
        rabbit --> message
        
        subgraph message-microservice[Message Service]
            message[Message]
        end
        
        gateway -->|PREFIX=/auth| auth[Auth]
        
        subgraph auth-microservice[Auth Service]
            auth --> db-auth[(Database)]
        end
        
        gateway -->|PREFIX=/visit| visit[Visit]
        
        subgraph visit-microservice[Visit Service]
            visit --> db-visit[(Database)]
        end
        
        gateway -->|PREFIX=/consule| consule[Consule]
        
        subgraph consule-microservice[Consule Service]
            consule --> db-consule[(Database)]
        end
    end
    
    consule --> export
    
    subgraph go [Golang]
        export[Export Service]
    end
```

### 端口管理

| 应用  | 微服务     | 端口    |
|-----|---------|-------|
| 网关  | gateway | 12100 |
| 认证  | auth    | 12101 |
| 短信  | message | 12102 |

## 部署架构

## 外部依赖

## 编码实践

### 统一返回体中响应枚举类的抽象设计

在分布式、微服务盛行的今天，绝大部分项目都采用的微服务框架与前后端分离方式。前端和后端进行交互，前端按照约定请求URL路径，并传入相关参数，后端服务器接收请求，进行业务处理，返回数据给前端。

维护一套完善且规范的接口是非常有必要的， 这样不仅能够提高对接效率，也可以让代码看起来更加简洁优雅。

#### 定义

- ***code***  -  由后端统一定义各种返回结果的**状态码**
- ***data***  -  本次返回的**数据**
- ***message***  -  本次接口调用的**结果描述**

其中，*code* 与 *message* 用于对后端处理的详情进行描述。

例如，前端发起 `GET localhost:8080/the_most_handsome_person` 请求（***HTTP Request***），后端返回以下响应体（***HTTP Response Body***）：

```json
{
    "code": 0,
    "data": {
        "name": "张三",
        "age": "20"
    },
    "message": "请求成功"
}
```

#### 高可扩展抽象设计

**在实际开发中，`Result` 类更多地是被放在共用的工具库中。**

然而，**每个不同的微服务/业务可能都有自己的状态码**，甚至每个接口都可能会有（根据自己的设计），而**每次调用 `Result.success(...)` 都会造成一段极为冗长的代码**。

观察到**每次代码调用都要调用枚举类型的 `getCode()` 与 `getMessage()` 方法**（例如 `return Result.success(zhangsan, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());`），故可以将这两个方法抽离成接口（***extract interface***）

```java
public interface ResultEnumerable {
    Integer getCode();

    String getMessage();

    @Override
    String toString();  /*  可选  */
}
```

自定义返回枚举类，并实现 `ResultEnumerable` 接口：

```java
public enum DefaultResultEnum implements ResultEnumerable {
    SUCCESS(0, "Success"), // 请求成功
    ERROR(1, "Error"); // 请求失败

    private final Integer code;

    private final String message;

    DefaultResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
```

对 `Result` 类增加参数类型为 `ResultEnumerable` 的方法，在运行时多态调用接口的方法：

```java
public class Result<T> implements Serializable {
    private static final Long serialVersionUID = 9192910608408209894L;
    
    private final T data;
    private final Integer code;
    private final String message;

    private Result(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    // 增加方法，参数类型为 ResultEnumerable 接口
    public static <T> Result<T> success(T data, ResultEnumerable resultEnum) {
        return new Result<>(data, resultEnum.getCode(), resultEnum.getMessage());
    }

    public static <T> Result<T> success(T data, int code, String message) {
        return new Result<>(data, code, message);
    }
        
    // 增加方法，参数类型为 ResultEnumerable 接口
    public static <T> Result<T> error(T data, ResultEnumerable resultEnum) {
        return new Result<>(data, resultEnum.getCode(), resultEnum.getMessage());
    }

    public static <T> Result<T> error(T data, int code, String message) {
        return new Result<>(data, code, message);
    }
}
```

此时，不需要再调用 `Result.success(zhangsan, ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());`，只需要**调用 `Result.success(zhangsan, ResultEnum.SUCCESS)`** 即可：

```java
class Person {
    private String name;
    private Integer age;
    
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    
    // 省略构造器、getter/setter、toString 等方法
}

@RestController
public class TestController {
    @GetMapping("/the_most_handsome_person")
 	public Result<?> theMostHandsomePerson() {
        Person zhangsan = new Person("张三", 20);
        
        return Result.success(zhangsan, ResultEnum.SUCCESS); // 使用枚举即可
    }   
}
```

#### 优点

##### 易用性极高

在实际开发中，**只需要将 `Result` 类与 `ResultEnumerable` 接口放在自己的项目代码中**即可，无其他依赖。

##### 可扩展性极高

在自己的实际业务中，**只需要实现 `ResultEnumerable` 接口**，就可以实现自己的枚举类！

```java
public enum UserResultEnum implements ResultEnumerable {
    SUCCESS(0, "Success"), // 请求成功
    USER_NOT_FOUND(1, "User not found"), // 没有该用户
    USER_INFO_IS_PRIVATE(2, "User's information is private"); // 用户信息是隐私

    private final Integer code;

    private final String message;

    DefaultResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
```

##### 可读性极高

**命名清晰的枚举可以清晰地表述实际信息**（例如 `USER_NOT_FOUND`）：

```java
import static com.example.demo.result.UserResultEnum.*;

@RestController
public class TestController {
    @GetMapping("/the_most_handsome_person")
 	public Result<?> theMostHandsomePerson() {
        Person zhangsan = new Person("张三", 20);
        
        // 代码内容：Result.success(zhangsan, SUCCESS)
        // 实际含义： 返回    成功     张三       成功
        // return Result.success(zhangsan, SUCCESS);
        
        // 代码内容：Result.error(null, USER_NOT_FOUND)
        // 实际含义： 返回    失败          没有该用户
        return Result.error(null, USER_NOT_FOUND);
    }   
}
```

该抽象设计以作为博客文章上传至网上：https://blog.csdn.net/m0_46261993/article/details/123253948?spm=1001.2014.3001.5501

## 环境搭建

### Nacos

参考 [nacos/setup.md](nacos/setup.md) 

### ELK

参考 [elk/elk.md](elk/setup.md)

### Redis

```shell
cd ./redis

docker-compose up -d
```

## 本地运行

### gateway

在执行 jar 时，增加命令行参数：
```shell
# Nacos 服务发现中心
--spring.cloud.nacos.discovery.server-addr=your-host:your-port

# Nacos 配置中心
--spring.cloud.nacos.config.server-addr=your-host:your-port
```

### auth

在执行 jar 时，增加命令行参数：
```shell
# Nacos 服务发现中心
--spring.cloud.nacos.discovery.server-addr=your-host:your-port

# Nacos 配置中心
--spring.cloud.nacos.config.server-addr=your-host:your-port
```

## FAQ
