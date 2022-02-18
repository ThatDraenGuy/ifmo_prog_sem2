package Collection;

import Collection.Classes.Dragon;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StorageHandlerTest {
    @Test
    public void correctFile() {
        File file = new File("./src/test/resources/testFile.json");
        StorageHandler testStorage = new StorageHandler(file);
        java.util.PriorityQueue<Dragon> collection = testStorage.load();
        System.out.println(collection.peek().getName());
        testStorage.save(collection);
    }

}