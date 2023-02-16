package server.services.commands;

import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.services.providers.AEArguments;
import server.services.providers.AECommands;
import server.services.proxy.ProxyDB;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AECommandService implements CommandService {
    private Map<String, String> input = null;
    private final ProxyDB proxyDB;
    public AECommandService(ProxyDB proxyDB) {
        this.proxyDB = proxyDB;
    }

    @Override
    public void handleCommand(String clientInput) {
        this.input = Stream.of(clientInput.split("--"))
                .map(str -> str.split(" "))
                .collect(Collectors.toMap(split -> split[0], split -> split.length > 1 ? split[1] : "command"));
        for (Map.Entry<String, String> entry: this.input.entrySet()) {
            if (Objects.equals("command", entry.getValue())) {
                switch (entry.getKey()) {
                    case AECommands.login -> login();
                    case AECommands.register -> register();
                    case AECommands.update_user -> updateUser();
                    case AECommands.reset_password -> resetPassword();
                    case AECommands.logout -> logout();
                    case AECommands.add_admin_user -> addAdminUser();
                    case AECommands.remove_admin_user -> removeAdminUser();
                    case AECommands.delete_user -> deleteUser();
                    default -> { /* todo: */
                    }
                }
            }
        }
        input.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    private void register() {
        this.proxyDB.register(
                this.input.get(AEArguments.username),
                this.input.get(AEArguments.password),
                this.input.get(AEArguments.first_name),
                this.input.get(AEArguments.last_name),
                this.input.get(AEArguments.email)
        );
        login();
    }

    private void login() {
        this.proxyDB.login(
                this.input.get(AEArguments.username),
                this.input.get(AEArguments.password));
    }

    private void updateUser() {
        // todo
        this.proxyDB.updateUser(
                this.input.get(AEArguments.session_id),
                this.input.get(AEArguments.new_username),
                this.input.get(AEArguments.new_first_name),
                this.input.get(AEArguments.new_last_name),
                this.input.get(AEArguments.new_email)
        );
    }

    private void resetPassword() {
        // todo
        this.proxyDB.resetPassword(
                this.input.get(AEArguments.session_id),
                this.input.get(AEArguments.username),
                this.input.get(AEArguments.old_password),
                this.input.get(AEArguments.new_password)
        );
    }

    private void logout() {
        // todo
        this.proxyDB.logout(this.input.get(AEArguments.session_id));
    }

    private void addAdminUser() {
        // todo
        this.proxyDB.addAdminUser(
                this.input.get(AEArguments.session_id),
                this.input.get(AEArguments.username));
    }

    private void removeAdminUser() {
        // todo
        this.proxyDB.removeAdminUser(
                this.input.get(AEArguments.session_id),
                this.input.get(AEArguments.username));
    }

    private void deleteUser() {
        // todo
        this.proxyDB.deleteUser(
                this.input.get(AEArguments.session_id),
                this.input.get(AEArguments.username));
    }
}
