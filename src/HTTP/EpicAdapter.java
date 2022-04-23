package HTTP;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;

import java.io.IOException;

public class EpicAdapter extends TypeAdapter<Epic> {

    @Override
    public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {

    }

    @Override
    public Epic read(final JsonReader jsonReader) throws IOException {
        return null;
    }
}
