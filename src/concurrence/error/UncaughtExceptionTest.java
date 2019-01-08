package concurrence.error;

/**
 * miracle
 * 2019/1/2 10:14
 */
public class UncaughtExceptionTest {


    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("uncaughtException捕捉到了异常！");
                    }
                });
                throw new NullPointerException(" 出现了错误 ！");
            }
        };
        new Thread(runnable).start();
    }


}

