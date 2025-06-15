package com.leosam.config.aot;


import com.leosam.SpringNativeDemoApplication;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeSerialization;

import java.util.List;

/**
 * 新增配置类LambdaRegistrationFeature,
 * 注意后续的Service以及实现类以及其他都需要添加注册类，
 * 否则运行时Lambda表达式会报错
 * <p>
 * Lambda注册特征（注册使用Lambda创建使用到的类，如Service中使用了MyBaitsPlus Query Lambda进行查询，则需要将Service注册进来）
 * 如果不注册编译无异常，运行时则会找不到
 */
public class LambdaRegistrationFeature implements Feature {

    @Override
    public void duringSetup(DuringSetupAccess access) {
        // Register all classes from com.leosam package for Lambda serialization support
        List<Class<?>> classesToRegister = List.of(
                // Main application
                SpringNativeDemoApplication.class

        );

        // Register these classes for Lambda serialization
        for (Class<?> clazz : classesToRegister) {
            RuntimeSerialization.registerLambdaCapturingClass(clazz);
        }
    }

}