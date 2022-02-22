package Collection;

import Collection.Classes.Dragon;
import org.json.JSONException;

import java.io.*;
import java.util.PriorityQueue;

public class StorageHandler {
    private File file;
    public StorageHandler(File file) {
        this.file=file;
    }
    public java.util.PriorityQueue<Dragon> load() {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            java.util.Scanner scanner = new java.util.Scanner(inputStream);
            String JSONString = new String();
            while (scanner.hasNextLine()) {
                JSONString += scanner.nextLine();
            }
            scanner.close();
            return JSONToCollection.parseString(JSONString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println("An exception occurred while reading from JSON file, initializing an empty collection...");
        }
        return new PriorityQueue<>();
    }
    public void save(java.util.PriorityQueue<Dragon> collection) throws IOException {
        //TODO fix?
        String JSONString = JSONToCollection.saveDragons(collection);
        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(JSONString.getBytes());
        bufferedOutputStream.close();
    }
}
