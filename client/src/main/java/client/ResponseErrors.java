package client;

public class ResponseErrors extends Exception {
    public ResponseErrors(String message) {
        super(message);
    }
    public ResponseErrors(String message, Throwable ex){
        super(message, ex);
    }
}

