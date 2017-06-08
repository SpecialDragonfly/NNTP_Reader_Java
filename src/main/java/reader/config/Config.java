package reader.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class Config {
    private YamlData yamlData;
    private FileTime lastModified;
    private String configLocation;

    public Config (String configLocation) throws IOException {

        this.configLocation = configLocation;
        this.yamlData = this.readConfig(configLocation);
        this.lastModified = this.configLastModifiedTime(configLocation);
    }

    public YamlData getConfig() throws IOException {
        if (this.hasChanged()) {
            this.yamlData = this.readConfig(this.configLocation);
        }
        return this.yamlData;
    }

    private FileTime configLastModifiedTime(String location) throws IOException {
        BasicFileAttributes attributes = Files.readAttributes(Paths.get(location), BasicFileAttributes.class);

        return attributes.lastModifiedTime();
    }

    private YamlData readConfig(String location) throws IOException {
        Yaml yamlFile = new Yaml(new Constructor(YamlData.class));
        InputStream input = new FileInputStream(new File(location));
        YamlData data = (YamlData) yamlFile.load(input);
        input.close();

        return data;
    }

    private boolean hasChanged() throws IOException {
        FileTime lastModified = this.configLastModifiedTime(this.configLocation);
        if (this.lastModified.compareTo(lastModified) != 0) {
            this.lastModified = lastModified;
            return true;
        }

        return false;
    }
}
