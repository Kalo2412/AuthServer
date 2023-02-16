package server.models;

public interface User {
    public UserData getUserData();
    public void updateUserData(String username,
                                  String password,
                                  String firstName,
                                  String lastName,
                                  String email);
    public void updateUserData(UserData userData);
    public boolean isAdmin();
    public boolean isAuthenticated();
    public void setAdmin(boolean isAdmin);
    public void setAuthenticated(boolean isAuthenticated);
}
