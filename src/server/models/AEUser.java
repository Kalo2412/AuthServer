package server.models;

public class AEUser implements User {
    private UserData userData;
    private boolean isAdmin = false;
    private boolean isAuthenticated = false;

    public AEUser(String username, String password, String firstName, String lastName, String email) {
        this.userData = new UserData(username, password, firstName, lastName, email);
    }

    @Override
    public UserData getUserData() {
        return this.userData;
    }

    @Override
    public void updateUserData(String username, String password, String firstName, String lastName, String email) {

    }

    @Override
    public void updateUserData(UserData userData) {
        this.userData = userData;
    }

    @Override
    public boolean isAdmin() {
        return this.isAdmin;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}

