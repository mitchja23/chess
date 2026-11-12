package client;
import chess.ChessBoard;
import model.AuthData;
import model.GameData;
import ui.BoardUI;

import java.util.List;
import java.util.Scanner;


public class Repl {
    private final ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);
    private State state = State.SIGNEDOUT;
    private String token;
    private List<GameData> games;

    public Repl(String url){
        server =  new ServerFacade(url);
    }
    private enum State{
        SIGNEDIN, SIGNEDOUT, QUIT
    }
    private String openGame(String username){
        return username == null ? "open" : username;
    }

    private void handlePreLogin(String input){
        String[] words = input.split(" ");
        if(words[0].equalsIgnoreCase("quit")){
            state = State.QUIT;
        }else if(words[0].equalsIgnoreCase("help")){
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
            System.out.println("login <USERNAME> <PASSWORD>");
            System.out.println("quit: quits application");
            System.out.println("help: command list");

        }else if(words[0].equalsIgnoreCase("register")){
            if(words.length == 4){
                try{
                    AuthData data = server.register(words[1],words[2],words[3]);
                    token = data.authToken();
                    state = State.SIGNEDIN;
                    System.out.println("Registered!");
                }catch (ResponseErrors e){
                    System.out.println("Could not register.");
                }
            }else{
                System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
            }
        }else if(words[0].equalsIgnoreCase("login")) {
            if (words.length == 3) {
                try{
                    AuthData data = server.login(words[1], words[2]);
                    token = data.authToken();
                    state = State.SIGNEDIN;
                    System.out.println("Welcome " + data.username());
                }catch (ResponseErrors e){
                    System.out.println("Could not login.");
                }
            } else {
                System.out.println("login <USERNAME> <PASSWORD>");
            }
        }else{
            System.out.println("Unknown Command");
        }
    }


    private void handlePostLogin(String input){
        String[] words = input.split(" ");
        if(words[0].equalsIgnoreCase("quit")){
            state = State.QUIT;
        }else if(words[0].equalsIgnoreCase("help")){
            System.out.println("create: <NAME>");
            System.out.println("list:");
            System.out.println("join: <NUMBER> white|black");
            System.out.println("observe: <NUMBER>");
            System.out.println("logout: ");
            System.out.println("help: command list");
            System.out.println("quit: quits application");

        }else if(words[0].equalsIgnoreCase("logout")){
            try{
                server.logout(token);
                token = null;
                state = State.SIGNEDOUT;
                System.out.println("logged out!");
            }catch (ResponseErrors e){
                System.out.println("Failed to logout");
            }
        }else if(words[0].equalsIgnoreCase("create")){
            if(words.length == 2){
                try{
                    server.createGame(token,words[1]);
                    System.out.println("Created game " + words[1]);
                }catch (ResponseErrors e){
                    System.out.println("Could not create Game.");
                }
            }else{
                System.out.println("create: <NAME>");
            }
        }else if(words[0].equalsIgnoreCase("list")) {
            if (words.length == 1) {
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
        }else if(words[0].equalsIgnoreCase("join")){
            if(words.length == 3){
                try{
                    int selectedGame = Integer.parseInt(words[1]);
                    if(games == null || selectedGame <1 || selectedGame > games.size()){
                        System.out.println("Invalid game");
                        return;
                    }
                    server.joinGame(token, words[2].toUpperCase(), games.get(selectedGame - 1).gameID());
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    BoardUI.createBoard(board, words[2].toUpperCase());
                }catch (NumberFormatException e){
                    System.out.println("Game must be a number.");
                } catch (ResponseErrors e) {
                    System.out.println("Could not join game.");
                }
            } else {
                System.out.println("Usage: join <NUMBER> <WHITE|BLACK>");
            }
        }else if(words[0].equalsIgnoreCase("observe")){
            if(words.length == 2){
                try{
                    int selectedGame = Integer.parseInt(words[1]);
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




        else{
            System.out.println("Unknown Command");
        }

    }


    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            if (state == State.SIGNEDOUT) {
                handlePreLogin(input);
            } else {
                handlePostLogin(input);
            }
        }
    }
}
