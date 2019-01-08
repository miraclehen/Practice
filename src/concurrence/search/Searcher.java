package concurrence.search;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * miracle
 * 2019/1/8 20:08
 */
public class Searcher {

    private static int QUEUE_FILE_SIZE = 10;
    private static BlockingQueue<File> queue = new ArrayBlockingQueue<File>(QUEUE_FILE_SIZE);
    private static int SEARCH_THREAD = 100;
    private static File DUMMY = new File("");

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("请输入文件路径：");
            String directorPath = in.nextLine();
            File dir = new File(directorPath);
            if (!dir.exists()) {
                throw new IllegalArgumentException("找不到对应目录!");
            }
            System.out.println("请输入查询的字符串关键字！");
            String keyword = in.nextLine();
            if (keyword == null || keyword.trim().equals("")) {
                throw new IllegalArgumentException("请输入合法的关键字！");
            }

            Runnable processFile = new Runnable() {
                @Override
                public void run() {
                    flatmapFile(dir);
                    try {
                        queue.put(DUMMY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(processFile).start();

            for (int i = 0; i < SEARCH_THREAD; i++) {
                Runnable search = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean done = false;
                            while (!done) {
                                File file = queue.take();
                                if (file == DUMMY) {
                                    done = true;
                                    queue.put(DUMMY);
                                } else {
                                    search(file, keyword);
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(search).start();
            }
        }
    }

    private static void search(File file, String keword) {
        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(keword)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void flatmapFile(File file) {
        try {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    flatmapFile(f);
                } else {
                    queue.put(f);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
