package server.database;

import server.models.User;

public interface DataBase {
    public void save(User user);
    public void delete(User user);
    public void update(User user);
    public User findUser();
}
