package server.models;

import java.util.Optional;

public class AEAuthenticatedUser implements User {
    private UserData userData;

    public AEAuthenticatedUser(String username,
                               String password,
                               String firstName,
                               String lastName,
                               String email) {

        this.userData = new UserData(username, password, firstName, lastName, email);
    }

    @Override
    public void updateUserData(String username,
                                  String password,
                                  String firstName,
                                  String lastName,
                                  String email) {

        this.userData = new UserData(
                Optional.ofNullable(username).orElse(this.userData.username()),
                Optional.ofNullable(password).orElse(this.userData.password()),
                Optional.ofNullable(firstName).orElse(this.userData.firstName()),
                Optional.ofNullable(lastName).orElse(this.userData.lastName()),
                Optional.ofNullable(email).orElse(this.userData.email()));
    }

    @Override
    public UserData getUserData() {
        return userData;
    }

    @Override
    public String blueprint() {
        return "Authenticated";
    }
}
