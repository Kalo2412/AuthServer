package server.services.database;

import server.models.User;
import server.models.UserData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AEDataBase implements DataBase {
    private final Set<User> allUsers;
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
    public void save(User user) {
        if(findUser(user) == null) {
            this.allUsers.add(user);
        }
        System.out.println(this.allUsers);
    }

    @Override
    public void delete(User user) {
        if (findUser(user) != null) {
            this.allUsers.remove(user);
        }
    }

    @Override
    public void update(User user, UserData userData) {
        String username = userData.username() == null ? user.getUserData().username() : userData.username();
        String password = userData.password() == null ? user.getUserData().password() : userData.password();
        String firstName = userData.firstName() == null ? user.getUserData().firstName() : userData.firstName();
        String lastName = userData.lastName() == null ? user.getUserData().lastName() : userData.lastName();
        String email = userData.email() == null ? user.getUserData().email() : userData.email();

        UserData userData1 = new UserData(username, password, firstName, lastName, email);
        user.updateUserData(userData1);
    }

    @Override
    public User findUser(User user) {
        return this.allUsers.stream()
                .filter(user1 -> Objects.equals(user1.getUserData(), user.getUserData()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findUser(String username, String password) {
        return this.allUsers.stream()
                .filter(user1 -> Objects.equals(user1.getUserData().username(), username)
                        && Objects.equals(user1.getUserData().password(), password))
                .findFirst()
                .orElse(null);
    }

    public User findUser(String username) {
        return this.allUsers.stream()
                .filter(user -> Objects.equals(user.getUserData().username(), username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateRights(User user, boolean shouldBeAdmin) {
        User user1 = findUser(user);
        if (user1 != null) {
            user1.setAdmin(shouldBeAdmin);
        }
    }
    @Override
    public boolean getRights(User user) {
        return user.isAdmin();
    }



}
