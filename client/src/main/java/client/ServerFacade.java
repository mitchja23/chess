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
import java.util.Map;
import java.util.List;

import model.GameData;
import model.UserData;



public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();

    private final Gson gson = new Gson();
    private final String serverUrl;
    private record GameList(List<GameData> games){};

    public ServerFacade(int port) {
        this("http://localhost:" + port);
    }

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private HttpResponse<String> makeRequest(String method, String path, String body, String authToken) throws ResponseErrors {
        String url =  serverUrl + path;
        try {
            var buildRequest = HttpRequest.newBuilder(URI.create(url))
                    .method(method, requestBodyPublisher(body));
            HttpRequest request;
            if (authToken == null) {
                request = buildRequest.build();
            } else {
                request = buildRequest.header("authorization", authToken).build();
            }
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
        makeRequest("DELETE", "/db", null, null);
    }

    public AuthData register(String username, String password, String email) throws ResponseErrors{
        var data = gson.toJson(new UserData(username, password, email));
        var response = makeRequest("POST", "/user", data, null);
        AuthData authResponse = gson.fromJson(response.body(), AuthData.class);
        return authResponse;
    }

    public AuthData login(String username, String password) throws ResponseErrors{
        var data = gson.toJson(new UserData(username, password, null));
        var response = makeRequest("POST", "/session", data, null);
        AuthData authResponse = gson.fromJson(response.body(), AuthData.class);
        return authResponse;
    }

    public void logout(String authToken) throws ResponseErrors{
        makeRequest("DELETE", "/session", null, authToken);
    }

    public int createGame(String authToken, String gameName) throws ResponseErrors{
        var body = Map.of("gameName", gameName);
        var data = gson.toJson(body);
        var response = makeRequest("POST", "/game", data, authToken);
        GameData gameResponse = gson.fromJson(response.body(), GameData.class);
        return gameResponse.gameID();
    }

    public List<GameData> listGames(String authToken) throws ResponseErrors{
        var response = makeRequest("GET", "/game", null, authToken);
        GameList gameResponse = gson.fromJson(response.body(), GameList.class);
        return gameResponse.games();
    }
}
