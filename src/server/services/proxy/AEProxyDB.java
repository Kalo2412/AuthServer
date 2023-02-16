package server.services.proxy;

import server.authenticator.AEAuthenticator;
import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.models.*;
import server.services.database.AEDataBase;
import server.services.database.DataBase;
import server.services.session.AESession;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class AEProxyDB implements ProxyDB {
    private final DataBase dataBase;
    private final Authenticator authenticator;
    private final Authorizator authorizator;
    private final ConcurrentMap<AESession, User> loggedUsers;
    public AEProxyDB(DataBase dataBase, Authenticator authenticator, Authorizator authorizator) {
        this.dataBase = dataBase;
        this.authenticator = authenticator;
        this.authorizator = authorizator;
        this.loggedUsers = new ConcurrentHashMap<>();
    }
    @Override
    public String register(String username, String password, String firstName, String lastName, String email) {
        User user = this.authenticator.authenticate(username, password, firstName, lastName, email);
        if (user == null) {
            user = new AEUser(username, password, firstName, lastName, email);
            user.setAuthenticated(true);
            AESession session = new AESession();
            this.loggedUsers.put(session, user);
            return session.getUniqueID();
        }
        return "";
    }

    @Override
    public String login(String username, String password) {
        User user = isUserLogged(username, password);
        if (user == null) {
            user = this.authenticator.authenticate(username, password);
            if (user != null) {
                user.setAuthenticated(true);
                AESession session = new AESession();
                this.loggedUsers.put(session, user);
                return session.getUniqueID();
            }
        }
        return "";
    }

    @Override
    public void login(String sessionId) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            if (!session.validate()) {
                this.loggedUsers.remove(session, this.loggedUsers.get(session));
                // todo
            }
        }
    }

    @Override
    public void updateUser(String sessionId, String newUsername, String newFirstName, String newLastname, String newEmail) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            if (!session.validate()) {
                this.loggedUsers.remove(session, this.loggedUsers.get(session));
                // todo
            } else {
                this.dataBase.update(this.loggedUsers.get(session),
                        new UserData(newUsername, null, newFirstName, newLastname, newEmail));
            }
        }
    }

    @Override
    public void resetPassword(String sessionId, String username, String oldPassword, String newPassword) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            if (!session.validate()) {
                this.loggedUsers.remove(session, this.loggedUsers.get(session));
                // todo
            } else {
                // todo
            }
        }
    }

    @Override
    public void logout(String sessionId) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            this.loggedUsers.get(session).setAuthenticated(false);
            this.loggedUsers.remove(session, this.loggedUsers.get(session));
        }
    }

    @Override
    public void addAdminUser(String sessionId, String userName) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            if (session.validate()) {
                if (this.authorizator.authorizate(this.loggedUsers.get(session))) {
                    User user = this.dataBase.findUser(userName);
                    if (user != null) {
                        this.dataBase.updateRights(user, true);
                    }
                }
            }
        }
    }

    @Override
    public void removeAdminUser(String sessionId, String userName) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            if (session.validate()) {
                if (this.authorizator.authorizate(this.loggedUsers.get(session))) {
                    User user = this.dataBase.findUser(userName);
                    if (user != null) {
                        this.dataBase.updateRights(user, false);
                    }
                }
            }
        }
    }

    @Override
    public void deleteUser(String sessionId, String userName) {
        AESession session = isValidSession(sessionId);
        if (session != null) {
            if (session.validate()) {
                if (this.authorizator.authorizate(this.loggedUsers.get(session))) {
                    User user = this.dataBase.findUser(userName);
                    if (user != null) {
                        this.dataBase.delete(user);
                        if (isUserLogged(userName, user.getUserData().password()) != null) {
                            removeUser(userName);
                        }
                    }
                }
            }
        }
    }

    private AESession isValidSession(String sessionID) {
        for (Map.Entry<AESession, User> entry : this.loggedUsers.entrySet()) {
            if (Objects.equals(entry.getKey().getUniqueID(), sessionID)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private User isUserLogged(String username, String password) {
        for (Map.Entry<AESession, User> entry : this.loggedUsers.entrySet()) {
            if (Objects.equals(entry.getValue().getUserData().username(), username) &&
                    Objects.equals(entry.getValue().getUserData().password(), password)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void removeUser(String userName) {
        for (Map.Entry<AESession, User> entry : this.loggedUsers.entrySet()) {
            if (Objects.equals(entry.getValue().getUserData().username(), userName)) {
                this.loggedUsers.remove(entry.getKey(), entry.getValue());
            }
        }
    }

}


