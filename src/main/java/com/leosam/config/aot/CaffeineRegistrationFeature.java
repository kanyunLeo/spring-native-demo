package com.leosam.config.aot;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

public class CaffeineRegistrationFeature implements Feature {

    private final AtomicBoolean triggered = new AtomicBoolean(false);

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        Class<?> caffeineCoreClazz = access.findClassByName("com.github.benmanes.caffeine.cache.Caffeine");
        access.registerReachabilityHandler(this::ensureCaffeineSupportEnabled, caffeineCoreClazz);
    }

    private void ensureCaffeineSupportEnabled(DuringAnalysisAccess duringAnalysisAccess) {
        final boolean needsEnablingYet = triggered.compareAndSet(false, true);
        if (needsEnablingYet) {
            registerCaffeineReflections(duringAnalysisAccess);
        }
    }

    private void registerCaffeineReflections(DuringAnalysisAccess duringAnalysisAccess) {
        final String[] needsHavingSimpleConstructors = typesNeedingConstructorsRegistered();
        for (String className : needsHavingSimpleConstructors) {
            registerForReflection(className, duringAnalysisAccess);
        }

        // 如果要分析状态，则需要这些
        for (String className : typesNeedingConstructorsRegisteredWhenRecordingStats()) {
            registerForReflection(className, duringAnalysisAccess);
        }
    }

    private void registerForReflection(
            String className,
            DuringAnalysisAccess duringAnalysisAccess) {
        final Class<?> aClass = duringAnalysisAccess.findClassByName(className);
        final Constructor<?>[] z = aClass.getDeclaredConstructors();
        RuntimeReflection.register(aClass);
        RuntimeReflection.register(z);
    }

    /**
     * This list is not complete, but a selection of the types we expect being most useful.
     * unfortunately registering all of them has been shown to have a very significant impact
     * on executable sizes. See https://github.com/quarkusio/quarkus/issues/12961
     */
    public static String[] typesNeedingConstructorsRegistered() {
        return new String[]{
                "com.github.benmanes.caffeine.cache.PDMS",
                "com.github.benmanes.caffeine.cache.PSA",
                "com.github.benmanes.caffeine.cache.PSMS",
                "com.github.benmanes.caffeine.cache.PSW",
                "com.github.benmanes.caffeine.cache.PSMW",
                "com.github.benmanes.caffeine.cache.PSAMW",
                "com.github.benmanes.caffeine.cache.PSAWMW",
                "com.github.benmanes.caffeine.cache.PSWMS",
                "com.github.benmanes.caffeine.cache.PSWMW",
                "com.github.benmanes.caffeine.cache.SILMS",
                "com.github.benmanes.caffeine.cache.SSA",
                "com.github.benmanes.caffeine.cache.SSLA",
                "com.github.benmanes.caffeine.cache.SSLMS",
                "com.github.benmanes.caffeine.cache.SSMS",
                "com.github.benmanes.caffeine.cache.SSMSA",
                "com.github.benmanes.caffeine.cache.SSMSAW",
                "com.github.benmanes.caffeine.cache.SSMSW",
                "com.github.benmanes.caffeine.cache.SSW",
        };
    }

    public static String[] typesNeedingConstructorsRegisteredWhenRecordingStats() {
        return new String[]{
                "com.github.benmanes.caffeine.cache.SILSMS",
                "com.github.benmanes.caffeine.cache.SSSA",
                "com.github.benmanes.caffeine.cache.SSLSA",
                "com.github.benmanes.caffeine.cache.SSLSMS",
                "com.github.benmanes.caffeine.cache.SSSMS",
                "com.github.benmanes.caffeine.cache.SSSMSA",
                "com.github.benmanes.caffeine.cache.SSSMSW",
                "com.github.benmanes.caffeine.cache.SSSMSAW",
                "com.github.benmanes.caffeine.cache.SSSW"
        };
    }
}
