# amaya-cors [![amaya-cors](https://img.shields.io/maven-central/v/io.github.amayaframework/amaya-cors?color=blue)](https://repo1.maven.org/maven2/io/github/amayaframework/amaya-cors)

CORS middleware and utilities for Amaya Framework. Provides configuration of allowed origins, methods, headers, 
credentials, and preflight handling.

## Getting Started

To install it, you will need:

* Java 11+
* Maven/Gradle
* Amaya Core or set of core modules

### Features

* Flexible configuration of allowed origins, including regex support
* Control of allowed HTTP methods and headers
* Support for preflight (OPTIONS) requests
* Automatic handling of CORS response headers,
  including `Access-Control-Allow-Credentials`, `Access-Control-Max-Age`, and `Access-Control-Expose-Headers`
* Fluent builder API for concise configuration
* Resettable configuration for reusability

## Installing

### Gradle dependency

```Groovy
dependencies {
    implementation group: 'io.github.amayaframework', name: 'amaya-core', version: '3.1.0'
    implementation group: 'io.github.amayaframework', name: 'amaya-cors', version: '1.0.0'
}
```

### Maven dependency

```xml
<dependencies>
    <dependency>
        <groupId>io.github.amayaframework</groupId>
        <artifactId>amaya-core</artifactId>
        <version>3.1.0</version>
    </dependency>
    <dependency>
        <groupId>io.github.amayaframework</groupId>
        <artifactId>amaya-cors</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Examples

### Configuring CORS globally

```java
import io.github.amayaframework.core.WebBuilders;
import io.github.amayaframework.cors.Cors;
import io.github.amayaframework.http.HttpMethod;

public class Main {
    public static void main(String[] args) throws Throwable {
        var app = WebBuilders.create()
                .withServerFactory(/*your server factory here*/)
                .configureApplication(Cors.applicationConfigurer(cfg -> {
                    cfg.allowedOrigins().allow("http://example.com", "https://another.com");
                    cfg.allowedMethods().allow(HttpMethod.GET, HttpMethod.POST);
                    cfg.allowedHeaders().allow("X-Test", "Authorization");
                    cfg.exposedHeaders().allow("X-Expose");
                    cfg.allowCredentials(true);
                    cfg.maxAge(3600);
                }))
                .build();

        app.bind(8080);

        app.configurer().add((ctx, next) -> {
            ctx.response().getWriter().println("Hello (now with CORS)");
        });

        app.run();
    }
}
```

### Using `allowAny` to permit all origins

```java
import io.github.amayaframework.core.WebBuilders;
import io.github.amayaframework.cors.Cors;
import io.github.amayaframework.http.HttpMethod;

public class Main {
    public static void main(String[] args) throws Throwable {
        var app = WebBuilders.create()
                .withServerFactory(/*your server factory here*/)
                .configureApplication(Cors.applicationConfigurer(cfg -> {
                    cfg.allowAny(); // Overrides specific origin/method/header settings
                }))
                .build();

        app.bind(8080);

        app.configurer().add((ctx, next) -> {
            ctx.response().getWriter().println("Hello (now with CORS)");
        });

        app.run();
    }
}
```

## Built With

* [Gradle](https://gradle.org) - Dependency management
* [amaya-core](https://github.com/AmayaFramework/amaya-core) - Various amaya modules

## Authors

* **[RomanQed](https://github.com/RomanQed)** - *Main work*

See also the list of [contributors](https://github.com/AmayaFramework/amaya-jetty/contributors)
who participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details
