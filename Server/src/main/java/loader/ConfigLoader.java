package loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Logger logger;
    private final Properties defaultProperties;

    public ConfigLoader() {
        this.logger = LoggerFactory.getLogger("loader.configLoader");
        this.defaultProperties = new Properties();
        defaultProperties.put("db_link", "localhost:5432/progdb");
        defaultProperties.put("db_user", "server");
        defaultProperties.put("db_password", "aboba");
        defaultProperties.put("username", "server");
        defaultProperties.put("password", "temp");
        defaultProperties.put("port", "2525");
    }

    public Properties load() {
        try {
            InputStream inputStream = new FileInputStream("config/config.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            if (checkProperties(properties)) {
                logger.info("Successfully loaded config...");
                return properties;
            }
            logger.warn("Loaded config was invalid, starting up with default config...");
        } catch (FileNotFoundException e) {
            logger.warn("Couldn't locate config file, starting up with default config...");
        } catch (IOException e) {
            logger.warn("An exception occurred while trying to load config, starting up with default config...");
        }
        return defaultProperties;
    }

    public boolean checkProperties(Properties properties) {
        return properties.containsKey("db_link") && properties.containsKey("db_user") && properties.containsKey("db_password") &&
                properties.containsKey("username") && properties.containsKey("password") && properties.containsKey("port");
    }
}
