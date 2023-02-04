package server.authenticator;

import server.models.User;
import server.services.database.DataBase;

public final class AEAuthenticator implements Authenticator {
    private final DataBase dataBase;
    public AEAuthenticator(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public void authenticate(String username, String password, String firstName, String lastName, String email) {

    }

    @Override
    public void authenticate(String username, String password) {

    }

    @Override
    public void authenticate(String sessionId) {

    }
}
