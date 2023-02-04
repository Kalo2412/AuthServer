package server.services.proxy;

import server.services.providers.AEArguments;
import server.services.session.AESession;

public interface ProxyDB {
    public void register(String username, String password, String firstName, String lastName, String email);
    public String login(String username, String password);
    public void login(String sessionId);
    public void updateUser(String sessionId,
                           String newUsername,
                           String newFirstName,
                           String newLastname,
                           String newEmail);
    public void resetPassword(String sessionId,
                              String username,
                              String oldPassword,
                              String newPassword);
    public void logout(String sessionId);
    public void addAdminUser(String sessionId,
                             String userName);
    public void removeAdminUser(String sessionId,
                                String userName);
    public void deleteUser(String sessionId,
                           String userName);

}
