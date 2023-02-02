package server.models;

public class AEUnauthenticatedUser implements User {
    @Override
    public String blueprint() {
        return "UnAuthenticated";
    }

    @Override
    public UserData getUserData() {
        return null;
    }

    @Override
    public void updateUserData(String username, String password, String firstName, String lastName, String email) {

    }
}
