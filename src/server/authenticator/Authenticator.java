package server.authenticator;

import server.models.User;

public interface Authenticator {
    public void authenticate(String username, String password, String firstName, String lastName, String email);
    public void authenticate(String username, String password);

    public void authenticate(String sessionId);

}
