package client;

import com.google.gson.Gson;
import model.AuthData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import model.UserData;


public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();

    private final Gson gson = new Gson();
    private final String serverUrl;

    public ServerFacade(int port) {
        this("http://localhost:" + port);
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private HttpResponse<String> makeRequest(String method, String path, String body) throws ResponseErrors {
        String url =  serverUrl + path;
        try {
            var request = HttpRequest.newBuilder(URI.create(url))
                    .method(method, requestBodyPublisher(body))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException ex) {
            throw new ResponseErrors("Could not make request", ex);
        }
    }

    private static BodyPublisher requestBodyPublisher(String body) throws IOException {
        if (body != null) {
            return BodyPublishers.ofString(body);
        } else {
            return BodyPublishers.noBody();
        }
    }

    public void clear() throws ResponseErrors{
        makeRequest("DELETE", "/db", null);
    }

    public AuthData register(String username, String password, String email) throws ResponseErrors{
        var data = gson.toJson(new UserData(username, password, email));
        var response = makeRequest("POST", "/user", data);
        AuthData authResponse = gson.fromJson(response.body(), AuthData.class);
        return authResponse;
    }
}
