package client;


public class ClientMain {
    public static void main(String[] args) {
        var url = "http:localhost:8000";
        if (args.length == 1){
            url = args[0];
        }
        new Repl(url).run();
    }
}
