package reader;

import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ArticleCountTask extends Task {
    private final String group;
    private final ConcurrentLinkedQueue<Task> queue;

    public ArticleCountTask(String group, ConcurrentLinkedQueue<Task> taskQueue) {
        super();
        this.group = group;
        this.queue = taskQueue;
    }

    @Override
    public String run(NNTPClient nntpClient) {
        try {
            NewsgroupInfo[] info = nntpClient.listNewsgroups(this.group);
            int first = info[0].getFirstArticle();
            int last = info[0].getLastArticle();

            for (int start = first; start <= last; start += 10000) {
                this.queue.add(new GetArticlesTask(this.group, start, start + 10000));
            }

            return this.group.concat(": ").concat(String.valueOf(info[0].getArticleCount()));
        } catch (IOException ioEx) {
            // Do nothing.
        }

        return "";
    }
}
