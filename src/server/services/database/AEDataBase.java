package server.services.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.exceptions.PasswordTheSameAsOldException;
import server.exceptions.UserExistsException;
import server.exceptions.UserNotFoundException;
import server.exceptions.WrongPasswordException;
import server.models.AEUser;
import server.models.User;
import server.models.UserData;
import server.services.providers.AEPasswordSHA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class AEDataBase implements DataBase {
    private final Set<User> allUsers;
    private static AEDataBase dataBase = null;
    private static final String FILENAME = "/Users/kaloyan1224/IdeaProjects/AuthenticationServer/assets/users.txt";

    private AEDataBase() {
        this.allUsers = new HashSet<>();
    }

    private AEDataBase(String fileName) {
        this.allUsers = loadUsers();
    }

    public static AEDataBase getDataBase() {
        if (dataBase == null) {
            dataBase = new AEDataBase(AEDataBase.FILENAME);
        }
        return dataBase;
    }

    public static AEDataBase getTestDataBase() {
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
        } else {
            saveUsers(this.allUsers);
        }
    }

    @Override
    public void update(User user, UserData userData) {
        String username = userData.username() == null ? user.getUserData().username() : userData.username();
        String firstName = userData.firstName() == null ? user.getUserData().firstName() : userData.firstName();
        String lastName = userData.lastName() == null ? user.getUserData().lastName() : userData.lastName();
        String email = userData.email() == null ? user.getUserData().email() : userData.email();
        user.updateUserData(username, user.getUserData().password(), firstName, lastName, email);
        saveUsers(this.allUsers);
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
        saveUsers(this.allUsers);
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
            saveUsers(this.allUsers);
        }
    }

    @Override
    public User findUser(String username, String password) throws UserNotFoundException {
        User userInDataBase = this.allUsers.stream()
                .filter(user1 -> Objects.equals(user1.getUserData().username(), username)
                        && Objects.equals(user1.getUserData().password(), AEPasswordSHA.hashPassword(password)))
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
        saveUsers(this.allUsers);
    }
    @Override
    public boolean getRights(User user) {
        return user.isAdmin();
    }




    public static void saveUsers(Set<User> users) {
        try (FileWriter writer = new FileWriter(FILENAME)) {
            for (User user : users) {
                // Serialize the user's UserData object
                String serializedData = serializeUserData(user.getUserData());

                // Write the serialized data to the file
                writer.write(serializedData + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<User> loadUsers() {
        Set<User> users = new HashSet<>();
        try (Scanner scanner = new Scanner(new File(FILENAME))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Deserialize the UserData object
                UserData userData = deserializeUserData(line);

                // Create a new User object with the deserialized UserData
                User user = new AEUser("","","","","");
                user.updateUserData(userData);

                // Add the user to the list
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static String serializeUserData(UserData userData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(userData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static UserData deserializeUserData(String serializedData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(serializedData, UserData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
