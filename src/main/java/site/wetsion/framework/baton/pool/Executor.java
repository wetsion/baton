package site.wetsion.framework.baton.pool;

import site.wetsion.framework.baton.task.Task;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 3:16 PM
 **/
public interface Executor {

    void execute(final Task<?> task);
}
