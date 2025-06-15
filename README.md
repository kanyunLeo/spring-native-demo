# spring-native-demo

## 打包为 JAR

在项目根目录下执行以下命令：

```bash
mvn clean package
```

## 使用追踪代理（Tracing Agent）

### Windows
```bash
java "-Dspring.aot.enabled=true" "-agentlib:native-image-agent=config-output-dir=./src/main/resources/META-INF/native-image" -jar "target/spring-native-demo-0.0.1-SNAPSHOT.jar"
```

### Mac
```bash
java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=./src/main/resources/META-INF/native-image -jar target/spring-native-demo-0.0.1-SNAPSHOT.jar
```



## 转换为 Native Image

### Windows
```bash
native-image -cp target/spring-native-demo-0.0.1-SNAPSHOT.jar 

native-image -cp target/spring-native-demo-0.0.1-SNAPSHOT.jar -H:Name=spring-native-demo -H:ReflectionConfigurationFiles=src/main/resources/META-INF/native-image/reflect-config.json -H:ResourceConfigurationFiles=src/main/resources/META-INF/native-image/resource-config.json -H:DynamicProxyConfigurationFiles=src/main/resources/META-INF/native-image/proxy-config.json
```

### Mac
```bash
native-image -cp target/spring-native-demo-0.0.1-SNAPSHOT.jar


native-image -cp target/spring-native-demo-0.0.1-SNAPSHOT.jar -H:Name=spring-native-demo -H:ReflectionConfigurationFiles=src/main/resources/META-INF/native-image/reflect-config.json -H:ResourceConfigurationFiles=src/main/resources/META-INF/native-image/resource-config.json -H:DynamicProxyConfigurationFiles=src/main/resources/META-INF/native-image/proxy-config.json
```

注意：
1. 确保已经安装了 GraalVM 和 native-image 工具
2. 转换后的可执行文件将生成在当前目录下，名为 `spring-native-demo`
3. 转换过程可能需要几分钟时间，请耐心等待