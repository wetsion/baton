package site.wetsion.framework.baton.datasource;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.task.Task;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 4:54 PM
 **/
@SPI("inMemory")
public interface TaskStore extends Store<Task> {

    /**
     * 弹出任务
     * @return 任务
     */
    Task pop();
}
