package reader.action;

import org.apache.commons.net.nntp.NNTPClient;
import reader.Worker;
import reader.config.Config;
import reader.config.Credentials;
import reader.repository.ReaderDatabase;
import reader.task.ArticleCountTask;
import reader.task.Task;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class ManageQueue {

    private NNTPClient client;
    private ConcurrentLinkedQueue<Task> taskQueue;
    private ReaderDatabase db;
    private Config config;

    public ManageQueue(NNTPClient client, ConcurrentLinkedQueue<Task> taskQueue, ReaderDatabase db, Config config) {
        this.client = client;
        this.taskQueue = taskQueue;
        this.db = db;
        this.config = config;
    }

    public void updateArticles() {
        try {
            ArrayList<Worker> workers = new ArrayList<>();
int count = 0;
            while (count == 0) {
                Credentials credentials = this.config.getConfig().credentials;
                CountDownLatch doneSignal = new CountDownLatch(credentials.workerCount);

                IntStream.range(0, credentials.workerCount).forEach((value) ->
                    workers.add(new Worker(this.client, credentials, this.taskQueue, doneSignal))
                );

                this.config.getConfig().groups.forEach((group) -> {
                    // Create worker task (cmd)
                    this.taskQueue.add(new ArticleCountTask(group, this.taskQueue, this.db));
                });

//                workers.forEach(Worker::run);
                doneSignal.await();
                count++;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


}
