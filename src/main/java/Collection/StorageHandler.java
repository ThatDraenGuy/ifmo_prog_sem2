package Collection;

import Collection.Classes.Dragon;

import java.io.*;
import org.json.*;

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
            JSONTokener tokenizedFile = new JSONTokener(JSONString);
            JSONArray dragonCollection = new JSONArray(tokenizedFile);
            return JSONToCollection.parseDragons(dragonCollection);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return null;
    }
}
