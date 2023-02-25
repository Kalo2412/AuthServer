package server.exceptions;

import org.mockito.internal.matchers.Not;

public class NotValidUsernameException extends RuntimeException {
    public NotValidUsernameException(String message) {
        super(message);
    }
}
