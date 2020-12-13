package site.wetsion.framework.baton;

import site.wetsion.framework.baton.datasource.ProgressStore;
import site.wetsion.framework.baton.datasource.TaskStore;
import site.wetsion.framework.baton.pool.TaskPool;
import site.wetsion.framework.baton.task.Task;
import site.wetsion.framework.baton.common.config.SpiExtensionLoader;
import site.wetsion.framework.baton.task.Progress;

import java.util.Objects;

/**
 * 任务入口，添加任务
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 2:07 PM
 **/
public class TaskEntrance {

    private static TaskPool TASK_POOL;

    private static TaskStore TASK_STORE;

    private static ProgressStore PROGRESS_STORE;

    private static void init() {
        if (Objects.isNull(TASK_POOL)) {
            TASK_POOL = (TaskPool) SpiExtensionLoader.getExtension(TaskPool.class);
        }
        if (Objects.isNull(PROGRESS_STORE)) {
            PROGRESS_STORE = (ProgressStore) SpiExtensionLoader.getExtension(ProgressStore.class);
        }
        if (Objects.isNull(TASK_STORE)) {
            TASK_STORE = (TaskStore) SpiExtensionLoader.getExtension(TaskStore.class);
        }
    }

    public static void entry(Task task) {
        init();
        TASK_STORE.save(task);
//        TASK_POOL.submit(task);
    }

    public static Progress queryProgress(String taskId) {
        return PROGRESS_STORE.getByTaskId(taskId);
    }
}
