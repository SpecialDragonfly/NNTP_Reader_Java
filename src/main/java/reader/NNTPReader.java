package reader;

import org.apache.commons.net.nntp.NNTPClient;
import org.eclipse.jetty.server.Server;
import reader.action.ManageQueue;
import reader.action.StartServer;
import reader.config.Config;
import reader.config.Db;
import reader.repository.ReaderDatabase;
import reader.servlet.ViewQueue;
import reader.task.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NNTPReader {
    public static void main(String[] args) {
        try {
            ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();

            StartServer startServer = new StartServer(new Server(8000));
            startServer.addServlet(new ViewQueue(taskQueue), "/*");

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

            ManageQueue manageQueueAction = new ManageQueue(client, taskQueue, db, config);
            manageQueueAction.updateArticles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
