package client;
import model.AuthData;

import java.util.Scanner;


public class Repl {
    private final ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);
    private State state = State.SIGNEDOUT;
    private String token;

    public Repl(String url){
        server =  new ServerFacade(url);
    }
    private enum State{
        SIGNEDIN, SIGNEDOUT, QUIT
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
