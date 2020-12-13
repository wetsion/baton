package site.wetsion.framework.baton.datasource.jvm;

import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.datasource.TaskStore;
import site.wetsion.framework.baton.task.Task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/13 6:28 PM
 **/
@SXIC("inMemory")
public class InMemoryTaskStore implements TaskStore {

    private Queue<Task> taskQueue = new ConcurrentLinkedQueue<>();

    @Override public void save(Task data) {
        taskQueue.offer(data);
    }

    @Override public Task pop() {
        return taskQueue.poll();
    }
}
