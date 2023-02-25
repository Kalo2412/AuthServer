package server.services.commands;

import server.exceptions.InvalidCommandParametersException;
import server.exceptions.InvalidCommandException;
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
    public String handleCommand(String clientInput) {
        this.input = Stream.of(clientInput.split("--"))
                .map(str -> str.split(" "))
                .collect(Collectors.toMap(split -> split[0], split -> split.length > 1 ? split[1] : "command"));
        for (Map.Entry<String, String> entry: this.input.entrySet()) {
            if (Objects.equals("command", entry.getValue())) {
                return switch (entry.getKey()) {
                    case AECommands.login -> login();
                    case AECommands.register -> register();
                    case AECommands.update_user -> updateUser();
                    case AECommands.reset_password -> resetPassword();
                    case AECommands.logout -> logout();
                    case AECommands.add_admin_user -> addAdminUser();
                    case AECommands.remove_admin_user -> removeAdminUser();
                    case AECommands.delete_user -> deleteUser();
                    default -> throw new InvalidCommandException("Command not found!");
                };
            }
        }
        input.forEach((key, value) -> System.out.println(key + ": " + value));
        return "";
    }

    private String register() {
        return this.proxyDB.register(
                isNotNull(AEArguments.username),
                isNotNull(AEArguments.password),
                isNotNull(AEArguments.first_name),
                isNotNull(AEArguments.last_name),
                isNotNull(AEArguments.email)
        );
    }

    private String login() {
        return this.proxyDB.login(
                isNotNull(AEArguments.username),
                isNotNull(AEArguments.password));
    }

    private String updateUser() {
        this.proxyDB.updateUser(
                isNotNull(AEArguments.session_id),
                isNotNull(AEArguments.new_username),
                isNotNull(AEArguments.new_first_name),
                isNotNull(AEArguments.new_last_name),
                isNotNull(AEArguments.new_email)
        );


        return "";
    }

    private String resetPassword() {
        this.proxyDB.resetPassword(
                isNotNull(AEArguments.session_id),
                isNotNull(AEArguments.username),
                isNotNull(AEArguments.old_password),
                isNotNull(AEArguments.new_password)
        );


        return "";
    }

    private String logout() {
        this.proxyDB.logout(isNotNull(AEArguments.session_id));

        return "";
    }

    private String addAdminUser() {
        this.proxyDB.addAdminUser(
                isNotNull(AEArguments.session_id),
                isNotNull(AEArguments.username));

        return "";
    }

    private String removeAdminUser() {
        this.proxyDB.removeAdminUser(
                isNotNull(AEArguments.session_id),
                isNotNull(AEArguments.username));

        return "";
    }

    private String deleteUser() {
        this.proxyDB.deleteUser(
                isNotNull(AEArguments.session_id),
                isNotNull(AEArguments.username));

        return "";
    }

    private String isNotNull(String arg) {
        if (this.input.get(arg) == null || this.input.get(arg).isEmpty()) {
            throw new InvalidCommandParametersException("Command does not have provided parameters!");
        }
        return this.input.get(arg);
    }
}
