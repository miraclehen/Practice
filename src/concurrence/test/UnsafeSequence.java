package concurrence.test;

/**
 * miracle
 * 2019/1/2 17:21
 */
public class UnsafeSequence {

    public static void main(String[] args) {
        Test test = new Test();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int next = test.getNext();
                    System.out.println(next);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int next = test.getNext();
                    System.out.println(next);
                }
            }
        }).start();

    }


    static class Test {
        private int value;

        public synchronized int getNext() {
            return value++;
        }
    }
}
