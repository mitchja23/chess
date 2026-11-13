package client;
import java.util.Scanner;


public class Repl {

    private final Scanner scanner = new Scanner(System.in);
    private final ClientHelperFunctions helpers;
    private State state = State.SIGNEDOUT;




    public Repl(String url){
        helpers = new ClientHelperFunctions(url);
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
            if(helpers.registerHelper(words)){
                state = State.SIGNEDIN;
            }

        }else if(words[0].equalsIgnoreCase("login")) {
            if(helpers.loginHelper(words)){
                state = State.SIGNEDIN;
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
            if(helpers.logoutHelper()){
                state = State.SIGNEDOUT;
            }
        }else if(words[0].equalsIgnoreCase("create")){
            helpers.createHelper(words);
        }else if(words[0].equalsIgnoreCase("list")) {
            helpers.listHelper(words);
        }else if(words[0].equalsIgnoreCase("join")){
            helpers.joinHelper(words);
        }else if(words[0].equalsIgnoreCase("observe")){
            helpers.observeHelper(words);
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
