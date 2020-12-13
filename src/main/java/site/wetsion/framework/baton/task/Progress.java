package site.wetsion.framework.baton.task;

import lombok.*;
import site.wetsion.framework.baton.common.enums.TaskStateEnum;

import java.io.Serializable;
import java.time.Instant;

/**
 * 任务进度
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 3:27 PM
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Progress implements Serializable {
    private static final long serialVersionUID = 195908338594237298L;

    private String taskId;
    private TaskStateEnum state;
    private long submitTime;
    private long executeTime;
    private long finishTime;
    private String errorCode;
    private String errorMsg;

    public Progress(String taskId) {
        this.taskId = taskId;
    }

    public long costedTime() {
        if (TaskStateEnum.SUCCEEDED.equals(state) || TaskStateEnum.FAILED.equals(state)) {
            return finishTime - executeTime;
        }
        if (TaskStateEnum.RUNNING.equals(state)) {
            return executeTime - submitTime;
        }
        return -1;
    }

    public boolean isFailed() {
        return TaskStateEnum.FAILED.equals(state);
    }

    public boolean isSucceeded() {
        return TaskStateEnum.SUCCEEDED.equals(state);
    }

    public boolean isRunning() {
        return !isFailed() && !isSucceeded();
    }

    public void ready() {
        this.setState(TaskStateEnum.WAITING);
        this.setSubmitTime(Instant.now().toEpochMilli());
    }

    public void running() {
        this.setState(TaskStateEnum.RUNNING);
        this.setExecuteTime(Instant.now().toEpochMilli());
    }

    public void succeeded() {
        this.setState(TaskStateEnum.SUCCEEDED);
        this.setFinishTime(Instant.now().toEpochMilli());
    }

    public void failed(Throwable e) {
        this.setState(TaskStateEnum.FAILED);
        this.setFinishTime(Instant.now().toEpochMilli());
        this.setErrorMsg(e.getMessage());
    }
}
