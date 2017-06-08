package reader.task;

import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

public class GetArticlesTask extends Task {
    private final String group;
    private final long start;
    private final long last;

    public GetArticlesTask(String group, long start, long last) {
        super();
        this.group = group;
        this.start = start;
        this.last = last;
    }

    @Override
    public String run(NNTPClient nntpClient) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(this.start);
            sb.append("-");
            sb.append(this.last);
            NewsgroupInfo testGroup = new NewsgroupInfo();
            nntpClient.selectNewsgroup(this.group, testGroup);
            nntpClient.xover(sb.toString());
        } catch (Exception ex) {
            // do nothing
        }

        return "";
    }
}
