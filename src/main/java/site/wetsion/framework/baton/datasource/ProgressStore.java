package site.wetsion.framework.baton.datasource;

import site.wetsion.framework.baton.common.annotation.SPI;
import site.wetsion.framework.baton.task.Progress;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 4:54 PM
 **/
@SPI("inMemory")
public interface ProgressStore extends Store<Progress> {

    /**
     * 根据任务ID获取进度
     * @param taskId 任务ID
     * @return 进度
     */
    Progress getByTaskId(String taskId);

    /**
     * 保存进度
     * @param progress
     */
    @Override void save(Progress progress);
}
