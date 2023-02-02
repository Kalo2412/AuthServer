package server.database;

import server.models.User;
import java.util.HashSet;
import java.util.Set;

public class AEDataBase implements DataBase {
    private Set<User> allUsers;

    public AEDataBase() {
        this.allUsers = new HashSet<>();
    }

    @Override
    public void save(User user) {
        // todo: check if user exists
        this.allUsers.add(user);
    }

    @Override
    public void delete(User user) {
        // todo: check if user exists
        this.allUsers.removeIf(us -> us.getUserData() == user.getUserData());
    }

    @Override
    public void update(User user) {
        // todo: check if user exists
    }

    @Override
    public User findUser() {
        return null;
    }
}
