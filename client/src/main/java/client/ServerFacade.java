package client;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

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

    private HttpResponse<String> makeRequest(String method, String path) throws ResponseErrors{
        try{
            String url =  serverUrl + path;
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).method(method, HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        }catch (IOException | InterruptedException ex){
            throw new ResponseErrors("Could not make request", ex);
        }
    }

    public void clear() throws ResponseErrors{
        makeRequest("DELETE", "/db");
    }

}
