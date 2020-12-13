package site.wetsion.framework.baton;

import lombok.extern.slf4j.Slf4j;
import site.wetsion.framework.baton.task.Task;
import site.wetsion.framework.baton.worker.Worker;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/13 1:34 AM
 **/
@Slf4j
public class TestWorker implements Worker<Object> {
    @Override public String getName() {
        return "test";
    }

    @Override public void work(Task<Object> task) throws Throwable {
        Thread.sleep(10000);
        log.info("test task work! ");
    }
}
