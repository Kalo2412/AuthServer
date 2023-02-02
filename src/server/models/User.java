package server.models;

public interface User {
    public String blueprint();
    public UserData getUserData();
    public void updateUserData(String username,
                                  String password,
                                  String firstName,
                                  String lastName,
                                  String email);
}
