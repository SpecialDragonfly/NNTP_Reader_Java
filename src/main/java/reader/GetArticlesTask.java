package reader;

import org.apache.commons.net.nntp.NNTPClient;
import org.apache.commons.net.nntp.NewsgroupInfo;

public class GetArticlesTask extends Task {
    private final String group;
    private final int start;
    private final int last;

    public GetArticlesTask(String group, int start, int last) {
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
