package site.wetsion.framework.baton.worker;

import site.wetsion.framework.baton.task.Task;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 4:09 PM
 **/
public class DefaultWorker implements Worker<Object> {

    @Override public String getName() {
        return "default-worker";
    }

    @Override public void work(Task<Object> task) throws Throwable {
        throw new IllegalAccessException("Worker not found");
    }
}
