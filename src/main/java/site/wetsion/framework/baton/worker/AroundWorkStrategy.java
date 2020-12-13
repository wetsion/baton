package site.wetsion.framework.baton.worker;

import site.wetsion.framework.baton.task.Task;

/**
 * 环绕 worker 执行任务前后的策略
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:04 PM
 **/
public interface AroundWorkStrategy {

    /**
     * 处理task
     * @param task  任务
     */
    void handle(final Task<?> task);
}
