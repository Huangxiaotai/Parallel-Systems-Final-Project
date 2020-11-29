public class Task {
    Runnable runnable;

    Task(Runnable runnable){
        this.runnable =runnable;
    }

    public void run(){
        this.runnable.run();
    }
}
