package site.wetsion.framework.baton.datasource.jvm;

import site.wetsion.framework.baton.common.annotation.SXIC;
import site.wetsion.framework.baton.task.Progress;
import site.wetsion.framework.baton.datasource.ProgressStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jvm内存方式存储进度
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 4:55 PM
 **/
@SXIC("inMemory")
public class InMemoryProgressStore implements ProgressStore {

    private final static Map<String, Progress> PROGRESS_CACHE = new ConcurrentHashMap<>(16);

    @Override public Progress getByTaskId(String taskId) {
        return PROGRESS_CACHE.get(taskId);
    }

    @Override public void save(Progress progress) {
        PROGRESS_CACHE.put(progress.getTaskId(), progress);
    }
}
