package client;
import chess.ChessBoard;
import model.AuthData;
import model.GameData;
import ui.BoardUI;

import java.util.List;

public class ClientHelperFunctions {
    private final ServerFacade server;
    private String token;
    private List<GameData> games;

    public ClientHelperFunctions(String url){
        server = new ServerFacade(url);
    }

    private String openGame(String username){
        return username == null ? "open" : username;
    }

    public boolean registerHelper(String[] args){
        if(args.length == 4){
            try{
                AuthData data = server.register(args[1],args[2],args[3]);
                token = data.authToken();
                System.out.println("Registered!");
                return true;
            }catch (ResponseErrors e){
                System.out.println("Could not register.");
            }
        }else{
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
        }
        return false;
    }

    public boolean loginHelper(String[] args){
        if (args.length == 3) {
            try{
                AuthData data = server.login(args[1], args[2]);
                token = data.authToken();
                System.out.println("Welcome " + data.username());
                return true;
            }catch (ResponseErrors e){
                System.out.println("Could not login.");
            }
        } else {
            System.out.println("login <USERNAME> <PASSWORD>");
        }
        return false;
    }

    public boolean logoutHelper(){
        try{
            server.logout(token);
            token = null;
            System.out.println("logged out!");
            return true;
        }catch (ResponseErrors e){
            System.out.println("Failed to logout");
        }
        return false;
    }

    public void createHelper(String[] args){
        if(args.length == 2){
            try{
                server.createGame(token,args[1]);
                System.out.println("Created game " + args[1]);
            }catch (ResponseErrors e){
                System.out.println("Could not create Game.");
            }
        }else{
            System.out.println("create: <NAME>");
        }
    }

    public void listHelper(String[] args){
        if (args.length == 1) {
            try{
                games = server.listGames(token);

                if(games.isEmpty()){
                    System.out.println("No current games");
                }
                for(int i = 0; i < games.size(); i++){

                    GameData game = games.get(i);
                    System.out.println("Game " + (i+1) + ". " + game.gameName() + " White: " + openGame(game.whiteUsername()) + " Black: " + openGame(game.blackUsername()));
                }

            }catch (ResponseErrors e){
                System.out.println("Could not list games.");
            }
        } else {
            System.out.println("List:");
        }
    }

    public void joinHelper(String[] args){
        if(args.length == 3){
            try{
                int selectedGame = Integer.parseInt(args[1]);
                if(games == null || selectedGame <1 || selectedGame > games.size()){
                    System.out.println("Invalid game");
                    return;
                }
                server.joinGame(token, args[2].toUpperCase(), games.get(selectedGame - 1).gameID());
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                BoardUI.createBoard(board, args[2].toUpperCase());
            }catch (NumberFormatException e){
                System.out.println("Game must be a number.");
            } catch (ResponseErrors e) {
                System.out.println("Could not join game.");
            }
        } else {
            System.out.println("Usage: join <NUMBER> <WHITE|BLACK>");
        }
    }

    public void observeHelper(String[] args){
        if(args.length == 2){
            try{
                int selectedGame = Integer.parseInt(args[1]);
                if(games == null || selectedGame <1 || selectedGame > games.size()){
                    System.out.println("Invalid game");
                    return;
                }
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                BoardUI.createBoard(board, "WHITE");
            }catch (NumberFormatException e){
                System.out.println("Game must be a number.");
            }
        } else {
            System.out.println("Usage: observe <NUMBER>");
        }
    }



}
