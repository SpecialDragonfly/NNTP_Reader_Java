package reader;

import org.apache.commons.net.nntp.NNTPClient;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class NNTPReader {
    public static void main(String[] args) {
        NNTPClient client = new NNTPClient();

        try {
            Yaml yamlFile = new Yaml(new Constructor(Config.class));
            InputStream input = new FileInputStream(new File("src/main/resources/config.yml"));
            Path path = Paths.get("src/main/resources/config.yml");
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            FileTime lastModified = attributes.lastModifiedTime();
            Config data = (Config) yamlFile.load(input);
            input.close();

            ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<>();
            ArrayList<Worker> workers = new ArrayList<>();

            while (true) {
                CountDownLatch doneSignal = new CountDownLatch(data.credentials.workerCount);
                BasicFileAttributes checkAttributes = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime checkLastModified = checkAttributes.lastModifiedTime();
                if (checkLastModified.compareTo(lastModified) != 0) {
                    // Reread Config.
                }

                data.groups.forEach((group) -> {
                    // Create worker task (cmd)
                    taskQueue.add(new ArticleCountTask(group, taskQueue));
                });

                IntStream.range(0, data.credentials.workerCount).forEach((value) -> {
                    workers.add(new Worker(client, data.credentials, taskQueue, doneSignal));
                });

                workers.forEach(Worker::run);
                doneSignal.await();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
