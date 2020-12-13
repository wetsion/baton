package site.wetsion.framework.baton.pool;

import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.task.Task;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 3:15 PM
 **/
@SXIC("simple")
public class SimpleTaskPoolExecutor extends AbstractTaskPool {

    @Override public void execute(Task<?> task) {
        EXECUTOR.execute(() -> doExecute(task));
    }
}
