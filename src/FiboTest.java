import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class FiboTest {

    private Worker[] workers;
    //private static final ThreadLocal<WorkerStealingScheduler> executorTracker = new ThreadLocal<>();
    public int N = 21;

    private void generateTasks(Task task, int workId) {
        workers[workId].push(task);
    }

    private void fibonacci(int id, int number, AtomicInteger result, int workerid) {
        workerid = ((Worker)Thread.currentThread()).getWorkId();

        if (number == 1 || number == 2) {
            result.addAndGet(1);
            //System.out.println("id:"+ id + "result:"+result.get()+"number:" + number);
            return;
        }
        //System.out.println("id:"+ id + "result:"+result.get()+"number:" + number);
        int leftId = 2 * id;
        int finalWorkerid = workerid;
        Runnable run1 = new Runnable() {
            @Override
            public void run() {
                fibonacci(leftId, number - 1, result, finalWorkerid);
            }
        };
        generateTasks(new Task(run1), finalWorkerid);

        final int rightId = leftId + 1;

        Runnable run2 = new Runnable() {
            @Override
            public void run() {
                fibonacci(rightId, number - 2, result, finalWorkerid);
            }
        };
        generateTasks(new Task(run2), finalWorkerid);

    }

    public void run() throws InterruptedException {
        int numOfWorkers = Runtime.getRuntime().availableProcessors();

        final AtomicInteger result = new AtomicInteger(0);
        final AtomicInteger startedWorkerCounter = new AtomicInteger(numOfWorkers);
        final CountDownLatch countdown = new CountDownLatch(numOfWorkers);

        workers = new Worker[numOfWorkers];

        for (int i = 0; i < numOfWorkers; i++) {
            workers[i] = new Worker(i, numOfWorkers, startedWorkerCounter, workers, countdown);
        }
        for (int i = 0; i < numOfWorkers; i++) {
            workers[i].start();
        }

        Runnable run = new Runnable() {
            @Override
            public void run() {
                fibonacci(1, N, result, 0);
            }
        };

        workers[0].push(new Task(run));
        countdown.await();
        //System.out.println("i am wait done");
        for (int i = 0; i < numOfWorkers; i++) {
            workers[i].interrupt();
        }
        System.out.println("the result is :" + result.get());

    }

}
