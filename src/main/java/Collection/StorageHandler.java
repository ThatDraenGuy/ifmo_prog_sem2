package Collection;

import Collection.Classes.Dragon;
import Collection.Classes.MainCollectible;
import org.json.JSONException;

import java.io.*;
import java.util.PriorityQueue;

public class StorageHandler {
    private File file;
    public StorageHandler(File file) {
        this.file=file;
    }
    public java.util.PriorityQueue<MainCollectible> load(Class<? extends MainCollectible> target) {
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            StringBuilder JSONString = new StringBuilder();
            while (scanner.hasNextLine()) {
                JSONString.append(scanner.nextLine());
            }
            scanner.close();
            return JSONToCollection.parseCollection(JSONString.toString(), target);
        } catch (IOException | NoSuchMethodException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println("An exception occurred while reading from JSON file, initializing an empty collection...");
        }
        return new PriorityQueue<>();
    }
    public void save(java.util.PriorityQueue<MainCollectible> collection) throws IOException {
        //TODO fix?
        String JSONString = JSONToCollection.saveDragons(collection);
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(JSONString.getBytes());
        bufferedOutputStream.close();
    }
}
