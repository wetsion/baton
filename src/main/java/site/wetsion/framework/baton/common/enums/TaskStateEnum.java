package site.wetsion.framework.baton.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 任务状态枚举
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 3:31 PM
 **/
@AllArgsConstructor
@Getter
@ToString
public enum TaskStateEnum {

    /**
     * 任务排队等待中
     */
    WAITING(0, "任务排队等待中"),

    /**
     * 任务进行中
     */
    RUNNING(1, "任务进行中"),

    /**
     * 任务成功
     */
    SUCCEEDED(2, "任务成功"),

    /**
     * 任务失败
     */
    FAILED(9, "任务失败");

    private int type;
    private String info;
}
