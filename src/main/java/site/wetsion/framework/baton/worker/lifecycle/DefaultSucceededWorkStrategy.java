package site.wetsion.framework.baton.worker.lifecycle;

import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.task.Task;
import site.wetsion.framework.baton.task.Progress;
import site.wetsion.framework.baton.datasource.ProgressStore;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:37 PM
 **/
@SXIC("default")
public class DefaultSucceededWorkStrategy implements SucceededWorkStrategy {

    private ProgressStore progressStore;

    @Override public void handle(Task<?> task) {
        Progress progress = progressStore.getByTaskId(task.getTaskId());
        progress.succeeded();
        progressStore.save(progress);
    }

    public void setProgressStore(ProgressStore progressStore) {
        this.progressStore = progressStore;
    }
}
