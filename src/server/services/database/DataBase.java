package server.services.database;

import server.models.User;
import server.models.UserData;

public interface DataBase {
    public void save(UserData userData);
    public void delete(UserData userData);
    public void update(User user, UserData updated);
    public UserData findUser(UserData userData);
    public UserData findUser(String username, String password);
    public String getRights(UserData userData);
}
