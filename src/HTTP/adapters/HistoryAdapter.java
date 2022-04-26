package HTTP.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends TypeAdapter<List<Task>> {


    @Override
    public void write(final JsonWriter out, final List<Task> tasks) throws IOException {
        out.beginArray();
        for (Task task:
             tasks) {
            out.value(task.getId());
        }
        out.endArray();
    }

    @Override
    public List<Task> read(final JsonReader in) throws IOException {
       List<Task> a = new ArrayList<>();
//        Task task = new Task();
//        in.beginArray();
//        task.setName(in.nextString());
//        task.setDescription(in.nextString());
//        task.setUin(Integer.parseInt(in.nextString()));
//        task.setStatus(Status.valueOf(in.nextString()));
//        task.setStartTime(LocalDateTime.parse(in.nextString()));
//        task.setDuration(Duration.parse(in.nextString()));
//        System.out.println();
//        in.endArray();
//
     return a;
    }
}
