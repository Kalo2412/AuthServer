package server.authorizator;

import server.models.User;
import server.services.database.DataBase;

import java.util.Objects;

public final class AEAuthorizator implements Authorizator {
    private DataBase dataBase;
    public AEAuthorizator(DataBase dataBase) {
        this.dataBase = dataBase;
    }
    @Override
    public void authorizate(User user, String blueprint) {
        if (!Objects.equals(user.blueprint(), blueprint)) {
            return;
        }
    }
}
