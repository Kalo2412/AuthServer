package server.models;

import org.jetbrains.annotations.Nullable;
import server.services.providers.AEPasswordSHA;

public record UserData(@Nullable String username,@Nullable String password,@Nullable String firstName,@Nullable String lastName,@Nullable String email) {
    @Override
    public String password() {
        return password == null ? null : AEPasswordSHA.hashPassword(password);
    }
}
