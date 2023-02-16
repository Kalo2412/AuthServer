package server.authenticator;

import server.models.AEUser;
import server.models.User;
import server.models.UserData;
import server.services.database.DataBase;

public final class AEAuthenticator implements Authenticator {
    private final DataBase dataBase;
    public AEAuthenticator(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public User authenticate(String username, String password, String firstName, String lastName, String email) {
        return this.dataBase.findUser(new AEUser(username, password, firstName, lastName, email));
    }

    @Override
    public User authenticate(String username, String password) {
        return this.dataBase.findUser(username, password);
    }

    @Override
    public User authenticate(String username) {
        return this.dataBase.findUser(username);
    }
}
