package server.authenticator;

import server.models.User;

public interface Authenticator {
    public void authenticate(User user);
}
