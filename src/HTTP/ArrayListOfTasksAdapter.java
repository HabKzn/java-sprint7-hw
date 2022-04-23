package HTTP;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public class ArrayListOfTasksAdapter extends TypeAdapter<ArrayList<Task>> {
    @Override
    public void write(final JsonWriter jsonWriter, final ArrayList<Task> tasks) throws IOException {

    }

    @Override
    public ArrayList<Task> read(final JsonReader jsonReader) throws IOException {
        return null;
    }
}
