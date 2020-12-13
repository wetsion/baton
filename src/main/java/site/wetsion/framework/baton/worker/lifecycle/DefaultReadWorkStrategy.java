package site.wetsion.framework.baton.worker.lifecycle;

import org.apache.commons.lang3.ObjectUtils;
import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.task.Progress;
import site.wetsion.framework.baton.task.Task;
import site.wetsion.framework.baton.datasource.ProgressStore;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 3:53 PM
 **/
@SXIC("default")
public class DefaultReadWorkStrategy implements ReadWorkStrategy {

    private ProgressStore progressStore;

    @Override public void handle(Task<?> task) {
        Progress progress = ObjectUtils.defaultIfNull(progressStore.getByTaskId(task.getTaskId()),
                new Progress(task.getTaskId()));
        progress.ready();
        progressStore.save(progress);
    }

    public void setProgressStore(ProgressStore progressStore) {
        this.progressStore = progressStore;
    }
}
