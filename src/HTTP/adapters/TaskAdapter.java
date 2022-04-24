package HTTP.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskAdapter extends TypeAdapter<Task> {
    @Override
    public void write(final JsonWriter out, final Task task) throws IOException {
        out.beginArray();
        out.value(task.getName());
        out.value(task.getDescription());
        out.value(task.getId());
        out.value(String.valueOf(task.getStatus()));
        out.value(String.valueOf(task.getStartTime()));
        out.value(String.valueOf(task.getDuration()));
       out.endArray();
    }

    @Override
    public Task read(final JsonReader in) throws IOException {
        final Task task = new Task();
        in.beginArray();
        task.setName(in.nextString());
        task.setDescription(in.nextString());
        task.setUin(Integer.parseInt(in.nextString()));
        task.setStatus(Status.valueOf(in.nextString()));
        task.setStartTime(LocalDateTime.parse(in.nextString()));
       task.setDuration(Duration.parse(in.nextString()));
        System.out.println();
        in.endArray();

        return task;
    }
}
