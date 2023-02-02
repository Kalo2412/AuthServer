package server.models;

public class AEAdmin extends AEAuthenticatedUser {
    public AEAdmin(String username, String password, String firstName, String lastName, String email) {
        super(username, password, firstName, lastName, email);
    }

    @Override
    public String blueprint() {
        return "Admin";
    }

}
