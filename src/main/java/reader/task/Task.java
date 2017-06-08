package reader.task;

import org.apache.commons.net.nntp.NNTPClient;

public abstract class Task {
    public abstract String run(NNTPClient nntpClient);
}
