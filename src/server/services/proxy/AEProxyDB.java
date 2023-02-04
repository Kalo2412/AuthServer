package server.services.proxy;

import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.services.database.AEDataBase;
import server.services.database.DataBase;

public final class AEProxyDB implements ProxyDB {
    private final DataBase dataBase;

    public AEProxyDB(DataBase dataBase) {
        this.dataBase = dataBase;
    }
    @Override
    public void register(String username, String password, String firstName, String lastName, String email) {

    }

    @Override
    public String login(String username, String password) {
        return null;
    }

    @Override
    public void login(String sessionId) {

    }

    @Override
    public void updateUser(String sessionId, String newUsername, String newFirstName, String newLastname, String newEmail) {

    }

    @Override
    public void resetPassword(String sessionId, String username, String oldPassword, String newPassword) {

    }

    @Override
    public void logout(String sessionId) {

    }

    @Override
    public void addAdminUser(String sessionId, String userName) {

    }

    @Override
    public void removeAdminUser(String sessionId, String userName) {

    }

    @Override
    public void deleteUser(String sessionId, String userName) {

    }
}
