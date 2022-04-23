package HTTP;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskAdapter extends TypeAdapter<SubTask> {
    @Override
    public void write(final JsonWriter out, final SubTask subTask) throws IOException {
        out.beginArray();
        out.value(subTask.getName());
        out.value(subTask.getDescription());
        out.value(subTask.getId());
        out.value(String.valueOf(subTask.getStatus()));
        out.value(String.valueOf(subTask.getStartTime()));
        out.value(String.valueOf(subTask.getDuration()));
        out.value(subTask.getEpic().getId());
        out.endArray();
    }

    @Override
    public SubTask read(final JsonReader in) throws IOException {
        final SubTask subTask = new SubTask();
        in.beginArray();
        subTask.setName(in.nextString());
        subTask.setDescription(in.nextString());
        subTask.setUin(Integer.parseInt(in.nextString()));
        subTask.setStatus(Status.valueOf(in.nextString()));
        subTask.setStartTime(LocalDateTime.parse(in.nextString()));
        subTask.setDuration(Duration.parse(in.nextString()));
        System.out.println();
        in.endArray();

        return subTask;
    }
}
