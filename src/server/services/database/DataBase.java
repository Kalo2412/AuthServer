package server.services.database;

import server.models.User;
import server.models.UserData;

public interface DataBase {
    public void save(User user);
    public void delete(User user);
    public void update(User user, UserData updated);
    public void updatePassword(User user, String oldPassword);
    public void register(User user);
    public User findUser(String username, String password);
    public User findUser(String username);
    public void updateRights(User user, boolean shouldBeAdmin);
    public boolean getRights(User user);
}
