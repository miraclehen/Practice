package concurrence;

/**
 * miracle
 * 2019/1/7 10:27
 */
public class JoinTest extends Thread {

    int i;
    Thread previousThread;


    public JoinTest(Thread previousThread, int i) {
        this.previousThread = previousThread;
        this.i = i;
    }

    @Override
    public void run() {
//        try {
//            previousThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(previousThread.getName()+" num: " + i);
    }

    public static void main(String[] args) {
        Thread previousThread = Thread.currentThread();
        System.out.println("thread name:"+previousThread.getName());
        for (int i = 0; i < 50; i++) {
            JoinTest joinTest = new JoinTest(previousThread, i);
            joinTest.start();
            previousThread = joinTest;
        }
    }
}
