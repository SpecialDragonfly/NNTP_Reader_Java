package reader;

import org.apache.commons.net.nntp.NNTPClient;
import reader.action.ManageQueue;
import reader.config.Config;
import reader.config.Db;
import reader.repository.ReaderDatabase;
import reader.task.Task;

import java.util.concurrent.*;

public class NNTPReader {
    public static void main(String[] args) {
        try {
            Config config = new Config("src/main/resources/config.yml");
            Db credentials = config.getConfig().db;
            MysqlConnection connection = new MysqlConnection(
                credentials.hostname,
                credentials.port,
                credentials.dbname,
                credentials.username,
                credentials.password
            );
            connection.connect();

            ReaderDatabase db = new ReaderDatabase(connection);
            NNTPClient client = new NNTPClient();
            ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();
            ManageQueue manageQueueAction = new ManageQueue(client, taskQueue, db, config);
            manageQueueAction.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
