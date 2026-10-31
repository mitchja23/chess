package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseConnector;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SqlGameDAO implements GameDAO {

    public SqlGameDAO() throws DataAccessException{
        var createTable = """
        CREATE TABLE IF NOT EXISTS game(
            gameID INT NOT NULL AUTO_INCREMENT,
            white VARCHAR(255),
            black VARCHAR(255),
            gameName VARCHAR(255) NOT NULL,
            game TEXT NOT NULL,
            PRIMARY KEY (gameID)
                        )""";
        DatabaseConnector.createTable(createTable);
    }



    @Override
    public int createGame(String game) throws DataAccessException {
        var createGame = """
                INSERT INTO game (gameName, game)
                VALUES (?,?)
                """;

        try (var connect = DatabaseConnector.getConnection();
             var newGame = connect.prepareStatement(createGame, Statement.RETURN_GENERATED_KEYS)) {
            newGame.setString(1, game);
            newGame.setString(2, new Gson().toJson(new ChessGame()));
            newGame.executeUpdate();

            var resultSet = newGame.getGeneratedKeys();
            var id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return id;
        } catch (SQLException ex) {
            throw new DataAccessException("could not create game", ex);
        }
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        var getGame = """
                SELECT gameID, white, black, gameName, game
                FROM game
                WHERE gameID = ?
                """;
        try (var connect = DatabaseConnector.getConnection();
             var currentGame = connect.prepareStatement(getGame)) {
            currentGame.setInt(1, id);
            try (var queryResult = currentGame.executeQuery()) {
                if (queryResult.next()) {
                    return new GameData(queryResult.getInt("gameID"), queryResult.getString("white"), queryResult.getString("black"), queryResult.getString("gameName"), new Gson().fromJson(queryResult.getString("game"), ChessGame.class));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("could not get game", ex);
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var updateGame = """
                UPDATE game
                SET white = ?, black = ?, gameName = ?, game = ?
                WHERE gameID = ?
                """;
        try (var connect = DatabaseConnector.getConnection();
             var putGame = connect.prepareStatement(updateGame)) {


            putGame.setString(1, gameData.whiteUsername());
            putGame.setString(2, gameData.blackUsername());
            putGame.setString(3, gameData.gameName());
            putGame.setString(4, new Gson().toJson(gameData.game()));
            putGame.setInt(5, gameData.gameID());
            putGame.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not update game", ex);
        }

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var listGame = """
                SELECT gameID, white, black, gameName, game
                FROM game
                """;
        var listedGames = new ArrayList<GameData>();
        try (var connect = DatabaseConnector.getConnection();
             var listedGame = connect.prepareStatement(listGame)) {


            try (var queryResult = listedGame.executeQuery()) {
                while (queryResult.next()) {
                    listedGames.add(new GameData(queryResult.getInt("gameID"), queryResult.getString("white"), queryResult.getString("black"), queryResult.getString("gameName"), new Gson().fromJson(queryResult.getString("game"), ChessGame.class)));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("could not list games", ex);
        }



        return listedGames;
    }

    @Override
    public void clear() throws DataAccessException {
        var clearGame = """
                DELETE FROM game
                """;
        try (var connect = DatabaseConnector.getConnection();
             var wipeGame = connect.prepareStatement(clearGame)) {
            wipeGame.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not clear game", ex);
        }


    }
}
