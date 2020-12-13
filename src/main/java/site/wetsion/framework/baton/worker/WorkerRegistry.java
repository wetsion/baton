package site.wetsion.framework.baton.worker;

import site.wetsion.framework.baton.task.Task;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工人注册器
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 3:58 PM
 **/
public class WorkerRegistry {

    private static final Map<String, Worker<?>> WORKERS_CACHE = new ConcurrentHashMap<>(16);

    /**
     * 根据任务查找工人
     * @param task 任务
     * @return 工人
     */
    @SuppressWarnings("unchecked")
    public static <T> Worker<T> lookup(Task<?> task) {
        if (Objects.isNull(task.getWorkerType())) {
            return (Worker<T>) new DefaultWorker();
        }
        Worker<?> worker = WORKERS_CACHE.getOrDefault(task.getWorkerType().getName(), new DefaultWorker());
        return (Worker<T>) worker;
    }

    /**
     * 注册工人
     * @param worker 工人
     */
    public static void register(Worker worker) {
        WORKERS_CACHE.putIfAbsent(worker.getWorkerType(), worker);
    }
}
