package services;

public final class Request {

    private Request() {
    }

    public record RegisterRequest(String username, String password, String email) {
    }

    public record LoginRequest(String username, String password) {
    }

    public record CreateGameRequest(String gameName) {
    }

    public record JoinGameRequest(String playerColor, Integer gameID) {
    }
}
