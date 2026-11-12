package client;
import java.util.Scanner;

public class Repl {
    private final ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);
    private State state = State.SIGNEDOUT;

    public Repl(String url){
        server =  new ServerFacade(url);
    }
    private enum State{
        SIGNEDIN, SIGNEDOUT, QUIT
    }
    private void handlePreLogin(String input){
        if(input.equalsIgnoreCase("quit")){
            state = State.QUIT;
        }else if(input.equalsIgnoreCase("help")){
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL>");
            System.out.println("login <USERNAME> <PASSWORD>");
            System.out.println("quit: quits application");
            System.out.println("help: command list");

        }
        else{
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
