package concurrence.counter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * miracle
 * 2018/12/29 16:27
 *
 * 计数器
 *
 * 输入在指定的目录下寻找带有指定字符串的文件个数
 */
public class MatchCounter implements Callable<Integer> {


    private File directory;
    private String keyword;
    private ExecutorService pool;
    private int count;

    public MatchCounter(File directory, String keyword, ExecutorService executor) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool = executor;
    }

    @Override
    public Integer call() throws Exception {
        count = 0;
        try {
            List<Future<Integer>> futureList = new ArrayList<>();
            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是目录的话，递归寻找
                    MatchCounter counter = new MatchCounter(file, keyword, pool);
                    Future<Integer> submit = pool.submit(counter);
                    futureList.add(submit);
                } else {
                    if (search(file)) {
                        count++;
                    }
                }
            }

            for (Future<Integer> future : futureList) {
                count += future.get();
            }

        } catch (InterruptedException e) {

        }
        return count;
    }

    private boolean search(File file) {
        try {
            try (Scanner in = new Scanner(file, "UTF-8")) {
                boolean found = false;
                while (!found && in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.contains(keyword)) {
                        found = true;
                    }
                }
                return found;
            }
        } catch (IOException e) {

        }
        return false;
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("输入一个目录:");
            String directory = in.nextLine();
            System.out.println("输入需要查找的关键字：");
            String keyword = in.nextLine();

            ExecutorService pool = Executors.newCachedThreadPool();

            MatchCounter counter = new MatchCounter(new File(directory), keyword, pool);
            Future<Integer> result = pool.submit(counter);

            try {
                System.out.println(result.get() + " matching files");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pool.shutdown();

            int largestPoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
            System.out.println("最大线程数为：" + largestPoolSize);
        }
    }
}
