package collection;

import collection.classes.MainCollectible;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * A class to handle collection's storage in a file.
 */
public class FileStorageHandler implements StorageHandler {
    private final File file;
    private final JsonHandler jsonHandler;
    private final Logger logger;

    public FileStorageHandler(File file, JsonHandler jsonHandler) {
        this.file = file;
        this.jsonHandler = jsonHandler;
        this.logger = LoggerFactory.getLogger("FileStorageHandler");
    }

    /**
     * A method to load collection from a file. Reads String form a file and sends it to {@link JsonHandler#parseCollection(String, Class)}
     *
     * @param target a target-class that defines what elements are stored in a collection
     * @return a list of deconstructed objects to be put in a collection
     */
    @Override
    public <T extends MainCollectible<T>> Collection<Map<String, Object>> load(Class<T> target) {
        try {
            StringBuilder JSONString = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                JSONString.append(scanner.nextLine());
            }
            scanner.close();
            return jsonHandler.parseCollection(JSONString.toString(), target);
        } catch (IOException e) {
            logger.warn(e.toString());
        } catch (JSONException e) {
            logger.warn("An exception occurred while reading from JSON file, initializing an empty collection..." + e);
        }
        return null;
    }

    /**
     * into a file.
     *
     * @param collection a list of already deconstructed object from collection
     * @param target     a target-class that defines what elements are stored in a collection
     * @throws IOException if there is an issue with writing information in a file.
     */
    @Override
    public <T extends MainCollectible<T>> void save(Collection<T> collection, Class<T> target) throws IOException {
        String JSONString = jsonHandler.serializeCollection(collection, target);
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(JSONString.getBytes());
        bufferedOutputStream.close();
    }
}
