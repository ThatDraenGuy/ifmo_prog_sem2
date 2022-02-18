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
            scanner.close();
            return JSONToCollection.parseString(JSONString);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void save(java.util.PriorityQueue<Dragon> collection) {
        String JSONString = JSONToCollection.saveDragons(collection);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(JSONString.getBytes());
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
