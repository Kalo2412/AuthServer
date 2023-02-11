package server.models;

import java.util.Objects;

public class AEAdmin extends AEAuthenticatedUser {
    public AEAdmin(String username, String password, String firstName, String lastName, String email) {
        super(username, password, firstName, lastName, email);
    }

    public AEAdmin(UserData userData) {
        super(userData);
    }

    @Override
    public String blueprint() {
        return "Admin";
    }

    @Override
    public void updateUserData(String username, String password, String firstName, String lastName, String email) {
        super.updateUserData(username, password, firstName, lastName, email);
    }

    @Override
    public void updateUserData(UserData userData) {
        super.updateUserData(userData);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
