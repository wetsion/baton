package site.wetsion.framework.baton.common.config;

import lombok.extern.slf4j.Slf4j;
import site.wetsion.framework.baton.datasource.TaskStore;
import site.wetsion.framework.baton.pool.TaskPool;
import site.wetsion.framework.baton.datasource.ProgressStore;
import site.wetsion.framework.baton.worker.lifecycle.*;

/**
 * 通用上下文环境初始器
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 4:19 PM
 **/
@Slf4j
public class GenericContextInitiator {

    public static void initContext() {
        initSpiExtensionLoader();
    }

    private static  void initSpiExtensionLoader() {
        initSpiExtensionLoader(TaskPool.class);
        initSpiExtensionLoader(ProgressStore.class);
        initSpiExtensionLoader(TaskStore.class);
        initSpiExtensionLoader(AfterWorkStrategy.class);
        initSpiExtensionLoader(BeforeWorkStrategy.class);
        initSpiExtensionLoader(FailedWorkStrategy.class);
        initSpiExtensionLoader(ReadWorkStrategy.class);
        initSpiExtensionLoader(SucceededWorkStrategy.class);
    }

    private static  void initSpiExtensionLoader(Class<?> type) {
        SpiExtensionLoader spiExtensionLoader = SpiExtensionLoader.getSpiExtensionLoader(type);
        spiExtensionLoader.build();
    }



}
