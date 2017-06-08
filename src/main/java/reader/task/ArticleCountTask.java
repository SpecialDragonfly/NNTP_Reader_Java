package reader.task;

import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;
import reader.repository.GroupRow;
import reader.repository.ReaderDatabase;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ArticleCountTask extends Task {
    private final String group;
    private final ConcurrentLinkedQueue<Task> queue;
    private ReaderDatabase db;

    public ArticleCountTask(String group, ConcurrentLinkedQueue<Task> taskQueue, ReaderDatabase db) {
        super();
        this.group = group;
        this.queue = taskQueue;
        this.db = db;
    }

    @Override
    public String run(NNTPClient nntpClient) {
        try {
            NewsgroupInfo[] info = nntpClient.listNewsgroups(this.group);

            for (int i = 0; i < info.length; i++) {
                String groupName = info[i].getNewsgroup();
                long first = info[i].getFirstArticleLong();
                long last = info[i].getLastArticleLong();

                for (long start = first; start <= last; start += 10000) {
                    if (start + 10000 <= last) {
                        this.queue.add(new GetArticlesTask(this.group, start, start + 10000));
                    } else {
                        this.queue.add(new GetArticlesTask(this.group, start, last));
                    }
                }
                this.db.saveGroup(
                    new GroupRow(
                        info[i].getNewsgroup(),
                        info[i].getPostingPermission() == 2,
                        info[i].getArticleCountLong()
                    )
                );

                System.out.println(groupName.concat(": ").concat(String.valueOf(info[0].getArticleCountLong())));
            }
        } catch (MalformedServerReplyException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
