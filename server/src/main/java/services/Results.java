package services;

import java.util.Collection;

public final class Results {

    private Results() {
    }
    public record GameSummary(int gameID, String whiteUsername, String blackUsername, String gameName){
    }
    public record AuthResult(String username, String authToken) {
    }
    public record CreateGameResult(int gameID) {
    }
    public record ListGamesResult(Collection<GameSummary> games) {
    }
    public record ErrorResult(String message){
    }


}
