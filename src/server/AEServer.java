package server;

import server.authenticator.AEAuthenticator;
import server.authenticator.Authenticator;
import server.authorizator.AEAuthorizator;
import server.authorizator.Authorizator;
import server.exceptions.*;
import server.models.User;
import server.services.commands.AECommandService;
import server.services.commands.CommandService;
import server.services.database.AEDataBase;
import server.services.proxy.AEProxyDB;
import server.services.proxy.ProxyDB;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class AEServer {
    public static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;

    private static final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public static void main(String[] args) {
        Authenticator authenticator = new AEAuthenticator(AEDataBase.getDataBase());
        Authorizator authorizator = new AEAuthorizator(AEDataBase.getDataBase());
        ProxyDB proxyDB = new AEProxyDB(AEDataBase.getDataBase(), authenticator, authorizator);
        CommandService commandService = new AECommandService(proxyDB);
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);



            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    // select() is blocking but may still return with 0, check javadoc
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        String clientInput = getClientInput(clientChannel);
                        System.out.println("Client: " + clientInput);

                        String output;

                        try {
                            output = commandService.handleCommand(clientInput);
                        } catch (InvalidCommandException | InvalidCommandParametersException |
                                NotValidUsernameException | PasswordTheSameAsOldException |
                                SessionExpiredException | UserExistsException | UserHasNoPermissionsException |
                                UserNotFoundException | WrongPasswordException e) {
                            output = e.getMessage();
                        }
                        writeClientOutput(clientChannel, output);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }


    private static String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private static void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }
}