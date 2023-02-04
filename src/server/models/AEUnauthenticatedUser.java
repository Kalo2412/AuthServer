package server.models;

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
}
