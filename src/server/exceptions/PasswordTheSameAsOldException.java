package server.exceptions;

public class PasswordTheSameAsOldException extends RuntimeException {
    public PasswordTheSameAsOldException(String message) {
        super(message);
    }
}
