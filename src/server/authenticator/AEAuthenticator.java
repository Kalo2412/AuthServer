package server.authenticator;

import server.exceptions.UserExistsException;
import server.exceptions.UserNotFoundException;
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
    public void authenticate(String username, String password, String firstName, String lastName, String email) throws UserExistsException {
        this.dataBase.register(new AEUser(username, password, firstName, lastName, email));
    }

    @Override
    public User authenticate(String username, String password) throws UserNotFoundException {
        return this.dataBase.findUser(username, password);
    }

    @Override
    public User authenticate(String username) throws UserNotFoundException {
        return this.dataBase.findUser(username);
    }
}
