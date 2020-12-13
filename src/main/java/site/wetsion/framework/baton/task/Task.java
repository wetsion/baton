package site.wetsion.framework.baton.task;

import java.io.Serializable;

/**
 * 任务
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 2:21 PM
 **/
public interface Task<T> extends Serializable {

    /**
     * 获取任务拥有者
     * @return
     */
    String getOwnerId();

    /**
     * 获取任务唯一标识
     * @return 任务唯一标识
     */
    String getTaskId();

    /**
     * 获取任务分片key
     * @return
     */
    String getShardingKey();

    Class getWorkerType();

    /**
     * 获取额外参数
     * @return
     */
    T getAdditionParam();
}
