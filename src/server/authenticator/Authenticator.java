package server.authenticator;

import server.models.AEAdmin;
import server.models.AEAuthenticatedUser;
import server.models.User;

public interface Authenticator {
    public AEAuthenticatedUser authenticate(String username, String password, String firstName, String lastName, String email);
    public AEAuthenticatedUser authenticate(String username, String password);

}
