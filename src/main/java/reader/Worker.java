package reader;

import org.apache.commons.net.nntp.NNTPClient;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

    private final Credentials credentials;
    private final ConcurrentLinkedQueue<Task> workList;
    private final NNTPClient nntpClient;
    private final CountDownLatch doneSignal;

    public Worker(
        NNTPClient nntpClient,
        Credentials credentials,
        ConcurrentLinkedQueue<Task> workList,
        CountDownLatch doneSignal
    ) {
        this.nntpClient = nntpClient;
        this.credentials = credentials;
        this.workList = workList;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        try {
            this.nntpClient.connect(this.credentials.hostname, this.credentials.port);
            this.nntpClient.authenticate(this.credentials.username, this.credentials.password);

            Task task;
            while (true) {
                synchronized (this.workList) {
                    task = this.workList.poll();
                    if (task == null) {
                        break;
                    }
                }
                task.run(this.nntpClient);
            }
            this.nntpClient.disconnect();
        } catch (Exception ioEx) {
            // Do nothing
        } finally {
            this.doneSignal.countDown();
        }
    }
}
