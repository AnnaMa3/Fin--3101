package exceptions;

public class BadConfigException extends RuntimeException{

    public BadConfigException(String message){
        super(message);
    }

    public BadConfigException(String message, Throwable cause){
        super(message, cause);
    }
}