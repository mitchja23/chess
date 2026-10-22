package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Results.CreateGameResult createGame(String authToken, Request.CreateGameRequest request) throws UnauthorizedException, BadRequestException, DataAccessException {
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: You are not authorized to create a game");
        }
        if(request.gameName() == null){
            throw new BadRequestException("Error: please enter a name");
        }
        int gameID = gameDAO.createGame(request.gameName());

        return new Results.CreateGameResult(gameID);
    }

    public Results.ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException {
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: You are not authorized to view game list");
        }
        ArrayList<Results.GameSummary> games = new ArrayList<>();
        for (GameData game: gameDAO.listGames()){
            games.add(new Results.GameSummary(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }

        return new Results.ListGamesResult(games);
    }

    public void joinGame(String authToken, Request.JoinGameRequest request) throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if(auth == null){
            throw new UnauthorizedException("Error: You are not authorized to join a game");
        }
        if(!"WHITE".equals(request.playerColor()) && !"BLACK".equals(request.playerColor())){
            throw new BadRequestException("Error: Issue with player color");
        }
        if(request.gameID() == null){
            throw new BadRequestException("Error: Invalid Game ID");
        }
        GameData game = gameDAO.getGame(request.gameID());
        if(game == null){
            throw new BadRequestException("Error: game not found");
        }
        if(("WHITE".equals(request.playerColor()) && game.whiteUsername() != null) || ("BLACK".equals(request.playerColor()) && game.blackUsername() != null)){
            throw new AlreadyTakenException("Error: Color taken");
        }
        if("WHITE".equals(request.playerColor())){
            gameDAO.updateGame(new GameData(game.gameID(), auth.username(), null, game.gameName(), game.game()));
        } else {
            gameDAO.updateGame(new GameData(game.gameID(), null, auth.username(), game.gameName(), game.game()));
        }
    }
}
