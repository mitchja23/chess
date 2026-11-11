package client;

import com.google.gson.Gson;

import java.net.http.HttpClient;

public class ServerFacade {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private final Gson gson = new Gson();
    private final String serverUrl;

    public ServerFacade(int port) {
        this("http://localhost:" + port);
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
