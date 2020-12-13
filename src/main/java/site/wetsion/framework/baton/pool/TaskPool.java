package site.wetsion.framework.baton.pool;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.task.Task;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 2:58 PM
 **/
@SPI("simple")
public interface TaskPool extends Pool, Executor {

    /**
     * 提交任务
     * @param task 任务
     */
    void submit(final Task task);
}
