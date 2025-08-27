[![](https://img.shields.io/badge/Maven%20Central-v1.0.0-brightgreen)](https://central.sonatype.com/artifact/io.github.benfromchina/jarvis-http/1.0.0)
[![](https://img.shields.io/badge/Release-v1.0.0-blue)](https://gitee.com/jarvis-lib/jarvis-http/releases/v1.0.0)
[![](https://img.shields.io/badge/License-Apache--2.0-9cf)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://img.shields.io/badge/JDK-8+-9cf)]()

# Jarvis-HTTP

Jarvis-HTTP is a Java client library designed to simplify HTTP requests and handle secure communication. It provides a comprehensive set of tools to support signed requests, response validation, data encryption, and more, making it suitable for applications that need to securely interact with HTTP services.

## Features

- **Secure Communication**: Supports request signing and response validation using RSA and SM algorithms to ensure communication security.
- **Data Encryption**: Provides AEAD and privacy encryption annotations for easy encryption and decryption of sensitive data.
- **Flexible Configuration**: Configure the client using the builder pattern, supporting custom connection timeout, read timeout, and other parameters.
- **Easy Integration**: Based on the Forest framework, supports automated HTTP client generation.

## Installation

### Maven

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.stark.jarvis</groupId>
    <artifactId>jarvis-http-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage Examples

### Create Configuration

You can create client configurations using `RSAConfig` or `SMConfig`. Below is an example using `RSAConfig`:

```java
Config config = new RSAConfig.Builder()
    .clientId("your-client-id")
    .clientSecret("your-client-secret")
    .clientCertFromPath("path/to/client-cert.pem")
    .clientPrivateKeyFromPath("path/to/client-private-key.pem")
    .serverPublicKeyFromPath("path/to/server-public-key.pem")
    .build();
```

### Create HTTP Client

Use `ServiceFactory` to create an HTTP client:

```java
ServiceFactory serviceFactory = ServiceFactory.builder()
    .config(config)
    .connectTimeout(5000)
    .readTimeout(10000)
    .build();

MyService myService = serviceFactory.client(MyService.class);
```

### Send Request

Define an interface to describe your HTTP request:

```java
public interface MyService {
    @Get("https://api.example.com/data")
    String getData();
}
```

Then call the method of this interface to send the request:

```java
String response = myService.getData();
System.out.println(response);
```

### Data Encryption and Decryption

You can use the `@AeadEncrypt` and `@PrivacyEncrypt` annotations to encrypt and decrypt data:

```java
@Data
@Accessors(chain = true)
public class User {
    private String id;

    @PrivacyEncrypt
    private String sensitiveData;
}
```

Data will be automatically encrypted before sending the request and automatically decrypted upon receiving the response.

## Contribution Guide

We welcome contributions from the community to Jarvis-HTTP. If you're interested in contributing to development or improving documentation, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-your-feature-name`).
3. Commit your changes (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin feature-your-feature-name`).
5. Create a new Pull Request.

## License

Jarvis-HTTP follows the MIT License. For details, please refer to the [LICENSE](LICENSE) file.

## Contact Us

If you have any questions or suggestions, please submit an Issue or contact the project maintainers.