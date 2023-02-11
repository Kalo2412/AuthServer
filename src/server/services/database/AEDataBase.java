package server.services.database;

import server.models.User;
import server.models.UserData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AEDataBase implements DataBase {
    private final Set<UserData> allUsers;
    private static AEDataBase dataBase = null;

    private AEDataBase() {
        this.allUsers = new HashSet<>();
    }

    public static AEDataBase getDataBase() {
        if (dataBase == null) {
            dataBase = new AEDataBase();
        }
        return dataBase;
    }

    @Override
    public void save(UserData userData) {
        if(findUser(userData) == null) {
            this.allUsers.add(userData);
        }
    }

    @Override
    public void delete(UserData userData) {
        // todo: check if user exists
        if (findUser(userData) != null) {
            this.allUsers.remove(userData);
        }
    }

    @Override
    public void update(User user, UserData userData) {
        delete(user.getUserData());
        save(userData);
    }

    @Override
    public UserData findUser(UserData userData) {
        return this.allUsers.stream()
                .filter(userData1 -> Objects.equals(userData1, userData))
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserData findUser(String username, String password) {
        return this.allUsers.stream()
                .filter(userData1 -> Objects.equals(userData1.username(), username)
                        && Objects.equals(userData1.password(), password))
                .findFirst()
                .orElse(null);
    }
    @Override
    public String getRights(UserData userData) {
        return null;
    }

}
