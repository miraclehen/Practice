package concurrence;

/**
 * miracle
 * 2019/1/7 11:01
 */
public class JoinTest2 extends Thread {

    int index = 0;

    public JoinTest2(int index){
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println(String.valueOf(index));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            JoinTest2 join = new JoinTest2(i);
            join.start();
        }
    }



}
