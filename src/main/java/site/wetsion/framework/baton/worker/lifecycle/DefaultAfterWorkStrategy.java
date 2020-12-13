package site.wetsion.framework.baton.worker.lifecycle;

import lombok.extern.slf4j.Slf4j;
import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.task.Task;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:28 PM
 **/
@SXIC("default")
@Slf4j
public class DefaultAfterWorkStrategy implements AfterWorkStrategy {
    @Override public void handle(Task<?> task) {
        log.debug("nothing");
    }
}
