package site.wetsion.framework.baton.worker.lifecycle;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.worker.AroundWorkStrategy;

/**
 * 准备工作
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:53 PM
 **/
@SPI("default")
public interface ReadWorkStrategy extends AroundWorkStrategy {
}
