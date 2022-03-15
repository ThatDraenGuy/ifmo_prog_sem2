package collection;

import collection.classes.MainCollectible;
import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A class to handle collection's storage in a file.
 */
public class StorageHandler {
    private File file;

    public StorageHandler(File file) {
        this.file = file;
    }

    /**
     * A method to load collection from a file. Reads String form a file and sends it to {@link JSONToCollection#parseCollection(String, Class)}
     *
     * @param target a target-class that defines what elements are stored in a collection
     * @return a list of deconstructed objects to be put in a collection
     */
    public ArrayList<HashMap<Field, Object>> load(Class<? extends MainCollectible> target) {
        try {
            StringBuilder JSONString = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                JSONString.append(scanner.nextLine());
            }
            scanner.close();
            return JSONToCollection.parseCollection(JSONString.toString(), target);
        } catch (IOException e) {
            System.out.println(e);
        } catch (JSONException e) {
            System.out.println("An exception occurred while reading from JSON file, initializing an empty collection..." + e);
        }
        return new ArrayList<>();
    }

    /**
     * A method to save collection in a file. Sends collection to {@link JSONToCollection#serializeCollection(ArrayList, Class)} and saves resulting String
     * into a file.
     *
     * @param collection a list of already deconstructed object from collection
     * @param target     a target-class that defines what elements are stored in a collection
     * @throws IOException if there is an issue with writing information in a file.
     */
    public void save(ArrayList<HashMap<Field, Object>> collection, Class<?> target) throws IOException {
        String JSONString = JSONToCollection.serializeCollection(collection, target);
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(JSONString.getBytes());
        bufferedOutputStream.close();
    }
}
