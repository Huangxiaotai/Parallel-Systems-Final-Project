import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.concurrent.atomic.AtomicInteger;


public class Worker extends Thread{
    private int numsOfWorkers;
    public int workerId;
    public Worker[] workers;
    public ConcurrentLinkedQueue<Task> localTasks = new ConcurrentLinkedQueue<Task>();
    private AtomicInteger countWorkers;
    private int target;

    public Worker(int workerId, int numsOfWorkers, AtomicInteger countWorkers, Worker[] workers) {
        this.numsOfWorkers = numsOfWorkers;
        this.workerId = workerId;
        this.workers = workers;
        this.localTasks = new ConcurrentLinkedQueue();
        this.countWorkers = countWorkers;
        this.target = findVictim(this.workerId);
    }


    // find a victim to steal
    private int findVictim(int workerId) {
        //System.out.println("find victim");
        return (workerId + 1) % numsOfWorkers;
    }


    // find a task either local task or others' task
    private Task getTask() {
        //System.out.println("getTask");

        // localPool is empty, try to steal others tasks
        // continue try to steal
        Task task= localTasks.poll();
        if (task != null) {
            return task;
        }
        int count = 0;
        while(count <= 100) {
            count++;
            Worker victim = workers[target];
            task = victim.steal();
            if (task != null) {
                System.out.println("steal done");
                return task;
            }
            target = findVictim(target);
            if (target == workerId) {
                target = findVictim(target);
            }
        }
        countWorkers.decrementAndGet();
        this.interrupt();
        return null;
    }

    public void push(Task task) {
        System.out.println("worker" +this.workerId+ "push");
        localTasks.offer(task);
    }

    private boolean finished() {
        //System.out.println("finished");
        return countWorkers.get() <= 0;
    }


    @Override
    public void run() {
        while(true) {
            Task task = getTask();
            if (task != null) {
                task.run();
            }
        }

    }


    public Task steal() {
        //System.out.println(workerId+ "work steal");
        return localTasks.poll();
    }

    public int getWorkId(){
        return workerId;
    }
}
