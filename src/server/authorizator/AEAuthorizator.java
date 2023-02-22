package server.authorizator;

import server.exceptions.UserHasNoPermissionsException;
import server.models.User;
import server.services.database.DataBase;

import java.util.Objects;

public final class AEAuthorizator implements Authorizator {
    private final DataBase dataBase;
    public AEAuthorizator(DataBase dataBase) {
        this.dataBase = dataBase;
    }
    @Override
    public void authorizate(User user) {
        if (!this.dataBase.getRights(user)) {
            throw new UserHasNoPermissionsException("This user is not admin!");
        }
    }
}
