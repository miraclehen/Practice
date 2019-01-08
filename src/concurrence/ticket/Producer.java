package concurrence.ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * miracle
 * 2019/1/7 14:50
 */
public class Producer extends Thread {

    List<Message> list = new ArrayList<>();
    int index = 0;

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(3000);
                Message msg = new Message();
                msg.msg = String.valueOf(index);
                index++;
                synchronized (list) {
                    list.add(msg);
                    list.notify();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Message waitMsg() {
        synchronized (list) {
            if (list.size() == 0) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return list.remove(0);
    }
}
