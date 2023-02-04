package server.services.commands;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AECommandService implements CommandService {
    private Map<String, String> input = null;
    @Override
    public void handleCommand(String clientInput) {
        this.input = Stream.of(clientInput.split("--"))
                .map(str -> str.split(" "))
                .collect(Collectors.toMap(split -> split[0], split -> split.length > 1 ? split[1] : "command"));
        input.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
