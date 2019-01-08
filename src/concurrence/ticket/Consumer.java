package concurrence.ticket;

/**
 * miracle
 * 2019/1/7 14:55
 */
public class Consumer extends Thread {

    Producer producer;

    public Consumer(String name, Producer producer) {
        super(name);
        this.producer = producer;
    }

    @Override
    public void run() {
        while (true) {
            Message msg = producer.waitMsg();
            System.out.println("Consumer "+getName() + " get a message:"+msg.msg);
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        producer.start();

        new Consumer("Consumer-1",producer).start();
        new Consumer("Consumer-2",producer).start();
        new Consumer("Consumer-3",producer).start();
    }
}
