package site.wetsion.framework.baton.common.util;

import lombok.experimental.UtilityClass;
import site.wetsion.framework.baton.common.constant.TaskConstant;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 5:00 PM
 **/
@UtilityClass
public class TaskUtil {

    public String generateProgressKey(String taskId) {
        return String.format(TaskConstant.TASK_PROGRESS_PREFIX, taskId);
    }
}
