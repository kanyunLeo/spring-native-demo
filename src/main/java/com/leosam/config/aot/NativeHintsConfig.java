package com.leosam.config.aot;

import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 搜集需要反射和序列化的类
 * 最好搜集一下，防止自己搜集搜集不完整
 */
@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(NativeHintsConfig.class)
public class NativeHintsConfig implements RuntimeHintsRegistrar {

    // 需要反射的包
    private final List<String> packageNameForReflectionList = List.of("com.leosam.pojo");
    // 需要序列化的包
    private final List<String> packageNameForSerializationList = List.of("com.leosam.pojo");

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
        // 注册可能要使用反射的类。 自动扫描
        // Register reflection metadata for all classes in packageNameList
        try {
            for (String packageName : packageNameForReflectionList) {
                for (Class<?> clazz : getClasses(packageName)) {
                    hints.reflection().registerType(clazz, MemberCategory.values());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to register reflection hints", e);
        }

        // 注册要序列化的类、必须rpc或者json序列化
        // Register serialization  类需要实现 java.io.Serializable
        try {
            for (String packageName : packageNameForSerializationList) {
                for (Class<?> clazz : getClasses(packageName)) {
                    // 使用 TypeReference 避免类型问题
                    hints.serialization().registerType(TypeReference.of(clazz));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to register serialization hints", e);
        }

    }

    private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        // This method should return all classes in the given package.
        // Implementation varies based on how you scan classes, e.g., using Reflections library.
        // Here, for illustration, you might use a library like Reflections to scan the package:
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        return classes.toArray(new Class<?>[0]);
    }

}