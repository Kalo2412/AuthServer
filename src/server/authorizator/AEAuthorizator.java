package server.authorizator;

import server.models.AEAdmin;
import server.models.AEAuthenticatedUser;
import server.models.User;
import server.services.database.DataBase;

import java.util.Objects;

public final class AEAuthorizator implements Authorizator {
    private final DataBase dataBase;
    public AEAuthorizator(DataBase dataBase) {
        this.dataBase = dataBase;
    }
    @Override
    public AEAuthenticatedUser authorizate(User user) {
        if (Objects.equals(this.dataBase.getRights(user.getUserData()), "admin")) {
            return new AEAdmin(user.getUserData());
        } else {
            return new AEAuthenticatedUser(user.getUserData());
        }
    }
}
