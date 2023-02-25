package server.services.proxy;

import server.authenticator.AEAuthenticator;
import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.exceptions.*;
import server.models.*;
import server.services.database.AEDataBase;
import server.services.database.DataBase;
import server.services.session.AESession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class AEProxyDB implements ProxyDB {
    private final DataBase dataBase;
    private final Authenticator authenticator;
    private final Authorizator authorizator;
    private final Map<AESession, User> loggedUsers;
    public AEProxyDB(DataBase dataBase, Authenticator authenticator, Authorizator authorizator) {
        this.dataBase = dataBase;
        this.authenticator = authenticator;
        this.authorizator = authorizator;
        this.loggedUsers = new HashMap<>();
    }
    @Override
    public String register(String username, String password, String firstName, String lastName, String email) throws UserExistsException {
        this.authenticator.authenticate(username, password, firstName, lastName, email);
        User user = this.authenticator.authenticate(username);
        user.setAuthenticated(true);
        AESession session = new AESession();
        this.loggedUsers.put(session, user);
        return session.getUniqueID();
    }

    @Override
    public String login(String username, String password) {
        User user = isUserLogged(username, password);
        AESession session = new AESession();
        if (user != null) {
            removeUser(username);
            user.setAuthenticated(true);
            this.loggedUsers.put(session, user);
        } else {
            User anotherUser = this.authenticator.authenticate(username, password);
            anotherUser.setAuthenticated(true);
            this.loggedUsers.put(session, anotherUser);
        }
        return session.getUniqueID();
    }

    @Override
    public void login(String sessionId) {
        isValidSession(sessionId);
    }

    @Override
    public void updateUser(String sessionId, String newUsername, String newFirstName, String newLastname, String newEmail) {
        AESession session = isValidSession(sessionId);
        this.dataBase.update(this.loggedUsers.get(session),
                    new UserData(newUsername, null, newFirstName, newLastname, newEmail));
    }

    @Override
    public void resetPassword(String sessionId, String username, String oldPassword, String newPassword) {
        AESession session = isValidSession(sessionId);
        User user = this.loggedUsers.get(session);
        if (!Objects.equals(user.getUserData().username(), username)) {
            throw new NotValidUsernameException("Invalid username!");
        }
        this.dataBase.updatePassword(user, oldPassword, newPassword);
    }

    @Override
    public void logout(String sessionId) {
        AESession session = isValidSession(sessionId);
        this.loggedUsers.get(session).setAuthenticated(false);
        this.loggedUsers.remove(session, this.loggedUsers.get(session));
    }

    @Override
    public void addAdminUser(String sessionId, String userName) {
        AESession session = isValidSession(sessionId);
        User user = this.loggedUsers.get(session);
        this.authorizator.authorizate(user);
        this.dataBase.updateRights(this.dataBase.findUser(userName), true);
    }

    @Override
    public void removeAdminUser(String sessionId, String userName) {
        AESession session = isValidSession(sessionId);
        User user = this.loggedUsers.get(session);
        this.authorizator.authorizate(user);
        this.dataBase.updateRights(this.dataBase.findUser(userName), false);
    }

    @Override
    public void deleteUser(String sessionId, String userName) {
        AESession session = isValidSession(sessionId);
        User user = this.loggedUsers.get(session);
        this.authorizator.authorizate(user);
        User userToDelete = this.dataBase.findUser(userName);
        if (userToDelete.isAuthenticated()) {
            removeUser(userName);
        }
        this.dataBase.delete(user);
    }

    private AESession isValidSession(String sessionID) {
        for (Map.Entry<AESession, User> entry : this.loggedUsers.entrySet()) {
            if (Objects.equals(entry.getKey().getUniqueID(), sessionID)) {
                if (!entry.getKey().validate()) {
                    entry.getValue().setAuthenticated(false);
                    this.loggedUsers.remove(entry.getKey(), entry.getValue());
                    throw new SessionExpiredException("Session expired");
                }
                return entry.getKey();
            }
        }
        throw new UserNotFoundException("User is not logged!");
    }

    private User isUserLogged(String username, String password) {
        for (Map.Entry<AESession, User> entry : this.loggedUsers.entrySet()) {
            if (Objects.equals(entry.getValue().getUserData().username(), username)) {
                    if (Objects.equals(entry.getValue().getUserData().password(), password)) {
                        return entry.getValue();
                    } else {
                        throw new WrongPasswordException("Wrong password!");
                    }
            }
        }
        return null;
    }

    private void removeUser(String userName) {
        for (Map.Entry<AESession, User> entry : this.loggedUsers.entrySet()) {
            if (Objects.equals(entry.getValue().getUserData().username(), userName)) {
                entry.getValue().setAuthenticated(false);
                this.loggedUsers.remove(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<AESession, User> getLoggedUsers() {
        return this.loggedUsers;
    }

}


