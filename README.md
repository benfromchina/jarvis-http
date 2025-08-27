# Jarvis-HTTP

Jarvis-HTTP 是一个用于简化 HTTP 请求和处理安全通信的 Java 客户端库。它提供了一套完整的工具来支持签名请求、响应验证、数据加密等功能，适用于需要与 HTTP 服务进行安全交互的应用场景。

## 特性

- **安全通信**：支持使用 RSA 和 SM 算法进行请求签名和响应验证，确保通信的安全性。
- **数据加密**：提供 AEAD 和隐私加密注解，方便对敏感数据进行加密和解密。
- **灵活配置**：通过构建器模式配置客户端，支持自定义连接超时、读取超时等参数。
- **易于集成**：基于 Forest 框架，支持自动化的 HTTP 客户端生成。

## 安装

### Maven

在你的 `pom.xml` 文件中添加以下依赖：

```xml
<dependency>
    <groupId>com.stark.jarvis</groupId>
    <artifactId>jarvis-http-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用示例

### 创建配置

你可以通过 `RSAConfig` 或 `SMConfig` 来创建客户端配置。以下是使用 `RSAConfig` 的示例：

```java
Config config = new RSAConfig.Builder()
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .clientCertFromPath("path/to/client-cert.pem")
    .clientPrivateKeyFromPath("path/to/client-private-key.pem")
    .serverPublicKeyFromPath("path/to/server-public-key.pem")
    .build();
```

### 创建 HTTP 客户端

使用 `ServiceFactory` 来创建 HTTP 客户端：

```java
ServiceFactory serviceFactory = ServiceFactory.builder()
    .config(config)
    .connectTimeout(5000)
    .readTimeout(10000)
    .build();

MyService myService = serviceFactory.client(MyService.class);
```

### 发送请求

定义一个接口来描述你的 HTTP 请求：

```java
public interface MyService {
    @Get("https://api.example.com/data")
    String getData();
}
```

然后调用该接口的方法来发送请求：

```java
String response = myService.getData();
System.out.println(response);
```

### 数据加密与解密

你可以使用 `@AeadEncrypt` 和 `@PrivacyEncrypt` 注解来对数据进行加密和解密：

```java
@Data
@Accessors(chain = true)
public class User {
    private String id;

    @PrivacyEncrypt
    private String sensitiveData;
}
```

在发送请求之前，数据会自动被加密；在接收响应时，数据会被自动解密。

## 贡献指南

我们欢迎社区成员为 Jarvis-HTTP 做出贡献。如果你有兴趣参与开发或改进文档，请遵循以下步骤：

1. Fork 仓库。
2. 创建新分支 (`git checkout -b feature-your-feature-name`).
3. 提交更改 (`git commit -am 'Add some feature'`).
4. 推送至分支 (`git push origin feature-your-feature-name`).
5. 创建新的 Pull Request.

## 许可证

Jarvis-HTTP 遵循 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 联系我们

如果你有任何问题或建议，请提交 Issue 或联系项目维护者。