package reader;

import org.apache.commons.net.nntp.NNTPClient;

public abstract class Task {
    public abstract String run(NNTPClient nntpClient);
}
