package server.services.database;

import server.exceptions.PasswordTheSameAsOldException;
import server.exceptions.UserExistsException;
import server.exceptions.UserNotFoundException;
import server.exceptions.WrongPasswordException;
import server.models.User;
import server.models.UserData;
import server.services.providers.AEPasswordSHA;

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
    public void delete(User user) {
        boolean existed = this.allUsers.remove(user);
        if(!existed) {
            throw new NullPointerException("Trying to remove user that does not exist");
        }
    }

    @Override
    public void update(User user, UserData userData) {
        String username = userData.username() == null ? user.getUserData().username() : userData.username();
        String firstName = userData.firstName() == null ? user.getUserData().firstName() : userData.firstName();
        String lastName = userData.lastName() == null ? user.getUserData().lastName() : userData.lastName();
        String email = userData.email() == null ? user.getUserData().email() : userData.email();
        user.updateUserData(username, user.getUserData().password(), firstName, lastName, email);
    }

    @Override
    public void updatePassword(User user, String oldPassword, String newPassword) {
        if (!Objects.equals(user.getUserData().password(), AEPasswordSHA.hashPassword(oldPassword))) {
            throw new WrongPasswordException("Wrong password!");
        }
        if (Objects.equals(oldPassword, newPassword)) {
            throw new PasswordTheSameAsOldException("The password is the same as old");
        }
        String username = user.getUserData().username();
        String firstName = user.getUserData().firstName();
        String lastName = user.getUserData().lastName();
        String email = user.getUserData().email();
        user.updateUserData(username, newPassword, firstName, lastName, email);
    }

    @Override
    public void register(User user) throws UserExistsException {
        User userInDataBase = this.allUsers.stream()
                .filter(user1 -> Objects.equals(user1.getUserData().username(), user.getUserData().username()))
                .findFirst()
                .orElse(null);
        if (userInDataBase != null) {
            throw new UserExistsException("This username is already taken.");
        } else {
            this.allUsers.add(user);
        }
    }

    @Override
    public User findUser(String username, String password) throws UserNotFoundException {
        User userInDataBase = this.allUsers.stream()
                .filter(user1 -> Objects.equals(user1.getUserData().username(), username)
                        && Objects.equals(user1.getUserData().password(), password))
                .findFirst()
                .orElse(null);
        if (userInDataBase == null) {
            throw new UserNotFoundException("This user is not in the database");
        }

        return userInDataBase;
    }

    public User findUser(String username) throws UserNotFoundException {
        User userInDataBase = this.allUsers.stream()
                .filter(user -> Objects.equals(user.getUserData().username(), username))
                .findFirst()
                .orElse(null);

        if (userInDataBase == null) {
            throw new UserNotFoundException("This user is not in the database");
        }

        return userInDataBase;
    }

    @Override
    public void updateRights(User user, boolean shouldBeAdmin) {
        User user1 = findUser(user.getUserData().username());
        user1.setAdmin(shouldBeAdmin);
    }
    @Override
    public boolean getRights(User user) {
        return user.isAdmin();
    }
}
