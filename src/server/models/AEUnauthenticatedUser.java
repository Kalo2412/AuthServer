package server.models;

import java.util.Objects;

public class AEUnauthenticatedUser implements User {
    private UserData userData;
    @Override
    public String blueprint() {
        return "UnAuthenticated";
    }

    @Override
    public UserData getUserData() {
        return this.userData;
    }

    @Override
    public void updateUserData(String username, String password, String firstName, String lastName, String email) {
        this.userData = new UserData(username, password, firstName, lastName, email);
    }

    @Override
    public void updateUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public int hashCode() {
        return this.userData.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AEUnauthenticatedUser) && !(obj instanceof AEAuthenticatedUser)) {
            return false;
        }
        return Objects.equals(this.userData, ((User) obj).getUserData());
    }
}
