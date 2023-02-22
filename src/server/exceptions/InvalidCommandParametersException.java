package server.exceptions;

public class InvalidCommandParametersException extends RuntimeException {
    public InvalidCommandParametersException(String message) {
        super(message);
    }
}
