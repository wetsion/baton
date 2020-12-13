package site.wetsion.framework.baton.task;

import lombok.Setter;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 2:22 PM
 **/
public class DefaultTask<T> implements Task {

    /**
     * 任务归属人
     */
    @Setter
    private String ownerId;

    /**
     * 任务的唯一标示
     */
    @Setter
    private String taskId;

    /**
     * 任务分片的key
     */
    @Setter
    private String shardingKey;

    @Setter
    private Class workerType;

    @Setter
    private T additionParam;

    @Override public String getOwnerId() {
        return ownerId;
    }

    @Override public String getTaskId() {
        return taskId;
    }

    @Override public String getShardingKey() {
        return shardingKey;
    }

    @Override public Class getWorkerType() {
        return workerType;
    }

    @Override public Object getAdditionParam() {
        return additionParam;
    }

}
