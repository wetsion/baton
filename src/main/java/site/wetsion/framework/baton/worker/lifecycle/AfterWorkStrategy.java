package site.wetsion.framework.baton.worker.lifecycle;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.worker.AroundWorkStrategy;

/**
 * 工作后
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:12 PM
 **/
@SPI("default")
public interface AfterWorkStrategy extends AroundWorkStrategy {
}
