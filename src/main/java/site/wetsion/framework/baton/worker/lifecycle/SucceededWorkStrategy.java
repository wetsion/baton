package site.wetsion.framework.baton.worker.lifecycle;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.worker.AroundWorkStrategy;

/**
 * 工作执行成功的处理
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:37 PM
 **/
@SPI("default")
public interface SucceededWorkStrategy extends AroundWorkStrategy {
}
