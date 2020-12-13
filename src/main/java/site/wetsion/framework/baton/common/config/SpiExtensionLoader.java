package site.wetsion.framework.baton.common.config;

import lombok.extern.slf4j.Slf4j;
import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.common.util.ClassUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 4:44 PM
 **/
@Slf4j
public class SpiExtensionLoader {

    private Class<?> type;

    private static final Map<Class<?>, SpiExtensionLoader> SPI_EXTENSION_LOADER_MAP = new ConcurrentHashMap<>(16);

    /**
     * 实例缓存
     */
    private final Map<String, Object> INSTANCE_CACHE = new ConcurrentHashMap<>();

    /**
     * 默认注入的实例缓存
     */
    private final static Map<Class<?>, Object> SELECTED_INSTANCE_CACHE = new ConcurrentHashMap<>();

    /**
     * 预注入的实例缓存，用于解决循环依赖
     */
    private final static Map<Class<?>, Object> SELECTED_INSTANCE_PRE_CACHE = new ConcurrentHashMap<>();

    public SpiExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public void build() {
        init();
        inject();
    }

    private void init() {
        List<Class<?>> implementations = ClassUtil.getAllClassByInterface(type);
        SPI spi = type.getAnnotation(SPI.class);
        Object instance;
        for (Class<?> clazz : implementations) {
            SXIC sxic = clazz.getAnnotation(SXIC.class);
            if (Objects.nonNull(sxic)) {
                try {
                    instance = clazz.newInstance();
                    INSTANCE_CACHE.put(clazz.getName(), instance);
                    if (spi.value().equals(sxic.value())) {
                        SELECTED_INSTANCE_PRE_CACHE.put(type, instance);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    log.warn("instance cache put fail", e);
                }
            }

        }
        // 再遍历一次，搜索selected设置为true的实现类
        for (Class<?> clazz : implementations) {
            SXIC sxic = clazz.getAnnotation(SXIC.class);
            if (Objects.nonNull(sxic) && sxic.selected()) {
                try {
                    instance = clazz.newInstance();
                    SELECTED_INSTANCE_PRE_CACHE.put(type, instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.warn("instance cache put fail", e);
                }
            }
        }

    }

    private void inject() {
        INSTANCE_CACHE.values().forEach(instance -> {
            for (Method method : instance.getClass().getMethods()) {
                if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
                    Class<?> paramClass = method.getParameterTypes()[0];
                    Object injectInstance = SELECTED_INSTANCE_PRE_CACHE.get(paramClass);
                    if (Objects.isNull(injectInstance)) {
                        // 还未初始化的属性
                        SpiExtensionLoader spiExtensionLoader = getSpiExtensionLoader(paramClass);
                        spiExtensionLoader.build();

                    }
                    injectInstance = SELECTED_INSTANCE_PRE_CACHE.get(paramClass);
                    try {
                        method.invoke(instance, injectInstance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("injected fail, type: {}, method: {}", type, method.getName(), e);
                    }
                }
            }
            INSTANCE_CACHE.put(instance.getClass().getName(), instance);
            SPI spi = type.getAnnotation(SPI.class);
            SXIC sxic = instance.getClass().getAnnotation(SXIC.class);

            if (Objects.nonNull(sxic) && (spi.value().equals(sxic.value()) || sxic.selected())) {
                SELECTED_INSTANCE_CACHE.put(type, instance);
            }
        });
    }

    public static Object getExtension(Class<?> type) {
        return SELECTED_INSTANCE_CACHE.get(type);
    }

    public static SpiExtensionLoader getSpiExtensionLoader(Class<?> type) {
        SpiExtensionLoader spiExtensionLoader = SPI_EXTENSION_LOADER_MAP.get(type);
        if (Objects.isNull(spiExtensionLoader)) {
            spiExtensionLoader = new SpiExtensionLoader(type);
            SPI_EXTENSION_LOADER_MAP.putIfAbsent(type, spiExtensionLoader);
        }
        return spiExtensionLoader;
    }

}
