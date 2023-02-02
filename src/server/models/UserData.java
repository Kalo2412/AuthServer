package server.models;

public record UserData(String username, String password, String firstName, String lastName, String email) {
    @Override
    public String password() {
        return "password";
    }
}
