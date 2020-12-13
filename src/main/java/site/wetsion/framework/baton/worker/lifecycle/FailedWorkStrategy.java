package site.wetsion.framework.baton.worker.lifecycle;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.worker.AroundWorkStrategy;

/**
 * 工作执行失败的处理
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:27 PM
 **/
@SPI("default")
public interface FailedWorkStrategy extends AroundWorkStrategy {

    /**
     * 失败处理
     * @param throwable 异常
     */
    void failedHandle(Throwable throwable);
}
