package server.authenticator;

import server.models.User;

public interface Authenticator {
    public User authenticate(String username, String password, String firstName, String lastName, String email);
    public User authenticate(String username, String password);
    public User authenticate(String username);

}
