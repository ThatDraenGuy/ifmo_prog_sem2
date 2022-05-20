package locales;

import lombok.Getter;

import java.util.ListResourceBundle;

public class Collectibles_en_IE extends ListResourceBundle {
    @Getter
    private final Object[][] contents = new Object[][]{
            {"id", "id"},
            {"name", "name"},
            {"coordinates", "coordinates"},
            {"age", "age"},
            {"color", "color"},
            {"type", " type"},
            {"character", "character"},
            {"cave", "cave"},
            {"creationDate", "creation date"},
            {"owner", "owner"},
            {"x", "x"},
            {"y", "y"},
            {"depth", "depth"}
    };
}
