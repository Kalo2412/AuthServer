package server.services.proxy;

import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.models.AEAuthenticatedUser;
import server.models.AEUnauthenticatedUser;
import server.models.User;
import server.models.UserData;
import server.services.database.AEDataBase;
import server.services.database.DataBase;
import server.services.session.AESession;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class AEProxyDB implements ProxyDB {
    private final DataBase dataBase;
    private final Authenticator authenticator;
    private final Authorizator authorizator;
    private final ConcurrentMap<AESession, AEAuthenticatedUser> loggedUsers;
    public AEProxyDB(DataBase dataBase, Authenticator authenticator, Authorizator authorizator) {
        this.dataBase = dataBase;
        this.authenticator = authenticator;
        this.authorizator = authorizator;
        this.loggedUsers = new ConcurrentHashMap<>();
    }
    @Override
    public void register(String username, String password, String firstName, String lastName, String email) {
        this.dataBase.save(new UserData(
                username,
                password,
                firstName,
                lastName,
                email
        ));
        this.login(username, password);
    }

    @Override
    public String login(String username, String password) {
        // todo check for session
        AEAuthenticatedUser aeAuthenticatedUser = this.authenticator.authenticate(username, password);
        return "";
    }

    @Override
    public void login(String sessionId) {
        // todo;
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

    private boolean isValidSession() {
        return true;
    }
}
