package HTTP;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class test {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        kvTaskClient klient = new kvTaskClient(new URI("http://localhost:8078"));
  String testJson = "{\"TYPE\":\"SUBTASK\",\"epic\":2,\"name\":\"подзадача 1.1\",\"description\":\"что-то маленькое и лёгкое 1.1\",\"id\":0,\"status\":\"NEW\"}";
        klient.put("1", testJson);
    }
}
