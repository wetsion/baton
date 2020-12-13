package site.wetsion.framework.baton.worker;

import site.wetsion.framework.baton.task.Task;

/**
 * 处理任务的工作者
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 2:33 PM
 **/
public interface Worker<T> {

    /**
     * 获取工作者的名称
     * @return 名称
     */
    String getName();

    /**
     * 获取工人的类型
     * @return 工人类
     */
    default String getWorkerType() {
        return getClass().getName();
    }

    /**
     * 开始工作处理任务
     *
     * @param task 任务
     * @throws Throwable 异常
     */
    void work(final Task<T> task) throws Throwable;
}
