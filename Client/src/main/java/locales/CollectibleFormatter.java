package locales;

import collection.classes.MainCollectible;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.FieldModel;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

public class CollectibleFormatter {

    public static <T> String formatData(FieldData fieldData, Class<T> dataClass, Object item) {
        if (item == null) return I18N.getCollectible("empty");
        if (Temporal.class.isAssignableFrom(dataClass)) {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy - hh:mm (zzzz)", I18N.getLocale().get());
            return formatter.format((TemporalAccessor) item);
        }
        if (dataClass.isEnum()) return I18N.getCollectible(dataClass.getSimpleName() + "." + item);
        return item.toString();
    }

    public static String getDescription(CollectibleModel collectible) {
        StringBuilder builder = new StringBuilder();
        for (String field : collectible.getValues().keySet()) {
            FieldModel model = collectible.getValues().get(field);
            if (!model.getFieldData().isUserReadable()) continue;
            builder.append(I18N.getCollectible(field)).append(": ");
            if (model.getFieldData().isCollectible())
                builder.append("{\n").append(getDescription(model.getCollectibleModel())).append("}\n");
            else
                builder.append(formatData(model.getFieldData(), model.getFieldData().getType(), model.getValue())).append("\n");
        }
        return builder.toString();
    }
}
