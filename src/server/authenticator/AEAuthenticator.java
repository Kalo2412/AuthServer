package server.authenticator;

import server.models.AEAuthenticatedUser;
import server.models.User;
import server.models.UserData;
import server.services.database.DataBase;

public final class AEAuthenticator implements Authenticator {
    private final DataBase dataBase;
    public AEAuthenticator(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public AEAuthenticatedUser authenticate(String username, String password, String firstName, String lastName, String email) {
        return new AEAuthenticatedUser(
                this.dataBase.findUser(new UserData(username, password, firstName, lastName, email))
        );
    }

    @Override
    public AEAuthenticatedUser authenticate(String username, String password) {
        return new AEAuthenticatedUser(this.dataBase.findUser(username, password));
    }
}
