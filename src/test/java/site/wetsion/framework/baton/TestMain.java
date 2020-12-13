package site.wetsion.framework.baton;

import site.wetsion.framework.baton.common.config.GenericContextInitiator;
import site.wetsion.framework.baton.task.DefaultTask;
import site.wetsion.framework.baton.worker.Worker;
import site.wetsion.framework.baton.worker.WorkerRegistry;

/**
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/11 5:59 PM
 **/
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        GenericContextInitiator.initContext();
        DefaultTask task = new DefaultTask();
        task.setTaskId("1");
        task.setShardingKey("a");
        TaskEntrance.entry(task);

        Worker testWorker = new TestWorker();
        WorkerRegistry.register(testWorker);
        DefaultTask task2 = new DefaultTask();
        task2.setTaskId("2");
        task2.setShardingKey("b");
        task2.setWorkerType(TestWorker.class);
        TaskEntrance.entry(task2);

        TaskDispatcher.start();

        Thread.sleep(2000);
        System.out.println(TaskEntrance.queryProgress("2").getState());
        Thread.sleep(12000);
        System.out.println(TaskEntrance.queryProgress("2").getState());

        TaskDispatcher.shutdown();
    }
}
