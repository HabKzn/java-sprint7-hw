import ServersAndClient.HttpTaskServer;
import ServersAndClient.KVServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();

    }
}

