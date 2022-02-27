package Collection;

import Collection.Classes.MainCollectible;
import org.json.JSONException;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageHandler {
    private File file;
    public StorageHandler(File file) {
        this.file=file;
    }
    public ArrayList<HashMap<Field, Object>> load(Class<? extends MainCollectible> target) {
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            StringBuilder JSONString = new StringBuilder();
            while (scanner.hasNextLine()) {
                JSONString.append(scanner.nextLine());
            }
            scanner.close();
            return JSONToCollection.parseCollection(JSONString.toString(), target);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println("An exception occurred while reading from JSON file, initializing an empty collection..."+e);
        }
        return new ArrayList<>();
    }
    public void save(ArrayList<HashMap<Field, Object>> collection, Class<?> target) throws IOException {
        //TODO fix?
        String JSONString = JSONToCollection.serializeCollection(collection, target);
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(JSONString.getBytes());
        bufferedOutputStream.close();
    }
}
