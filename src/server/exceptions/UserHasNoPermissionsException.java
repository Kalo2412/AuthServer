package server.exceptions;

public class UserHasNoPermissionsException extends RuntimeException {
    public UserHasNoPermissionsException(String message) {
        super(message);
    }
}
