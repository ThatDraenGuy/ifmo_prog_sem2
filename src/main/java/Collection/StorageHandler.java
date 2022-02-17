package Collection;

import Collection.Classes.Dragon;

import java.io.*;

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
            return JSONToCollection.parseString(JSONString);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return null;
    }
}
