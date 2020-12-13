package site.wetsion.framework.baton.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import site.wetsion.framework.baton.datasource.ProgressStore;
import site.wetsion.framework.baton.task.Task;
import site.wetsion.framework.baton.worker.Worker;
import site.wetsion.framework.baton.worker.lifecycle.*;
import site.wetsion.framework.baton.common.constant.PoolConstant;
import site.wetsion.framework.baton.common.constant.TaskConstant;
import site.wetsion.framework.baton.common.enums.TaskStateEnum;
import site.wetsion.framework.baton.task.Progress;
import site.wetsion.framework.baton.worker.WorkerRegistry;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 3:10 PM
 **/
@Slf4j
public abstract class AbstractTaskPool implements TaskPool {

    private ReadWorkStrategy readWorkStrategy ;

    public void setReadWorkStrategy(ReadWorkStrategy readWorkStrategy) {
        this.readWorkStrategy = readWorkStrategy;
    }

    private BeforeWorkStrategy beforeWorkStrategy;

    public void setBeforeWorkStrategy(BeforeWorkStrategy beforeWorkStrategy) {
        this.beforeWorkStrategy = beforeWorkStrategy;
    }

    private AfterWorkStrategy afterWorkStrategy;

    public void setAfterWorkStrategy(AfterWorkStrategy afterWorkStrategy) {
        this.afterWorkStrategy = afterWorkStrategy;
    }

    private FailedWorkStrategy failedWorkStrategy;

    public void setFailedWorkStrategy(FailedWorkStrategy failedWorkStrategy) {
        this.failedWorkStrategy = failedWorkStrategy;
    }

    private SucceededWorkStrategy succeededWorkStrategy;

    public void setSucceededWorkStrategy(SucceededWorkStrategy succeededWorkStrategy) {
        this.succeededWorkStrategy = succeededWorkStrategy;
    }

    private ProgressStore progressStore;

    public void setProgressStore(ProgressStore progressStore) {
        this.progressStore = progressStore;
    }

    static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<Runnable>(20000) {
        private static final long serialVersionUID = 4075780202327467522L;

        @SuppressWarnings("all")
        @Override
        public boolean offer(Runnable runnable) {
            if (EXECUTOR.getPoolSize() < PoolConstant.POOL_MAX_SIZE) {
                return false;
            }
            return super.offer(runnable);
        }
    };

    /**
     * 最下层是调用的JDK的 ThreadPoolExecutor 来运行任务
     */
    static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(PoolConstant.POOL_CORE_POOL_SIZE,
            PoolConstant.POOL_MAX_SIZE, PoolConstant.KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, QUEUE, (r) -> new Thread(r, TaskConstant.TASK_WORKER_NAME),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 真正执行任务的地方
     * @param task 任务
     * @param <T> 任务泛型
     */
    <T> void doExecute(final Task<T> task) {
        log.info("doExecuteTask start, {}", task);
        final Instant startTime = Instant.now();
        final Worker<T> worker = WorkerRegistry.lookup(task);
        try {
            before(task);
            worker.work(task);
            succeeded(task);
        } catch (Throwable e) {
            log.error("doExecuteTask error, work name: {}, {}", worker.getName(), task, e);
            failed(task, e);
        } finally {
            final long cost = Duration.between(startTime, Instant.now()).toMillis();
            log.info("doExecuteTask end, work name: {}, {}, 耗时:{}ms", worker.getName(), task, cost);
            after(task);
        }
    }

    @Override public void submit(Task task) {
        validateTask(task);
        if (!isRunning(task)) {
            ready(task);
            execute(task);
        }
    }

    @Override
    public void shutdown() {
        try {
            EXECUTOR.shutdown();
            if (!EXECUTOR.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("TaskPool did not terminate in 5 seconds, " + EXECUTOR.getTaskCount()
                        + " Tasks did not quit normally。");
            }
        } catch (InterruptedException ie) {
            EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void validateTask(Task task) {
        Objects.requireNonNull(task);
        if (StringUtils.isEmpty(task.getTaskId()) || StringUtils.isEmpty(task.getShardingKey())) {
            throw new NullPointerException("task id or shardingKey can not be null");
        }
    }

    private Boolean isRunning(Task task) {
        Progress progress = progressStore.getByTaskId(task.getTaskId());
        return Objects.nonNull(progress) && TaskStateEnum.RUNNING.equals(progress.getState());
    }

    private void ready(final Task<?> task) {
        readWorkStrategy.handle(task);
    }

    private void before(final Task<?> task) {
        beforeWorkStrategy.handle(task);
    }

    private void after(final Task<?> task) {
        afterWorkStrategy.handle(task);
    }

    private void failed(final Task<?> task, Throwable e) {
        failedWorkStrategy.handle(task);
        failedWorkStrategy.failedHandle(e);
    }

    private void succeeded(Task<?> task) {
        succeededWorkStrategy.handle(task);
    }

}
