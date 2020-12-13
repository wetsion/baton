package site.wetsion.framework.baton;

import lombok.extern.slf4j.Slf4j;
import site.wetsion.framework.baton.common.config.SpiExtensionLoader;
import site.wetsion.framework.baton.common.constant.PoolConstant;
import site.wetsion.framework.baton.datasource.TaskStore;
import site.wetsion.framework.baton.pool.TaskPool;
import site.wetsion.framework.baton.task.Task;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务调度器
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/13 6:49 PM
 **/
@Slf4j
public class TaskDispatcher {

    /**
     * 调度器是否在工作
     */
    private final static AtomicBoolean WORKING = new AtomicBoolean(false);

    /**
     * 调度器启动线程池，只供调度器使用
     */
    private static final ThreadPoolExecutor DISPATCHER_STARTER = new ThreadPoolExecutor(1,
            1, 60,
            TimeUnit.SECONDS, new LinkedBlockingDeque<>(), (r) -> new Thread(r, PoolConstant.DISPATCHER_STATER_NAME),
            new AbortPolicy());

    private static TaskPool taskPool;

    private static TaskStore taskStore;

    /**
     * 调度器开始工作
     */
    public static void start() {
        init();
        DISPATCHER_STARTER.execute(() -> {
            if (WORKING.compareAndSet(false, true)) {
                log.info("Started task dispatcher");

                delay(PoolConstant.DISPATCHER_STATER_DELAY_MILLIS);

                while (WORKING.get()) {
                    doDispatch();
                    delay(PoolConstant.DISPATCHER_STATER_DELAY_MILLIS);
                }
            }
        });
    }

    /**
     * 关闭调度器，同时关闭任务池
     */
    public synchronized static void shutdown() {
        log.info("stop task dispatcher...");
        WORKING.set(false);
        DISPATCHER_STARTER.shutdown();
        taskPool.shutdown();
    }

    private static void init() {
        log.info("task dispatcher init...");
        taskPool = (TaskPool) SpiExtensionLoader.getExtension(TaskPool.class);
        taskStore = (TaskStore) SpiExtensionLoader.getExtension(TaskStore.class);
    }

    /**
     * 调度器真正开始执行任务调度
     */
    private static void doDispatch() {
        Task task = taskStore.pop();
        try {
            if (Objects.nonNull(task)) {
                taskPool.submit(task);
            }
        } catch (Exception e) {
            log.error("task submit fail, save and try again next time", e);
            taskStore.save(task);
        }

    }

    private static void delay(long timeout) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    static class AbortPolicy implements RejectedExecutionHandler {

        public AbortPolicy() { }

        @Override public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task Dispatcher can only start one time!");
        }
    }
}
