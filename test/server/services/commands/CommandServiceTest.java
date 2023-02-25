package server.services.commands;

import io.activej.inject.annotation.Inject;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.models.AEUser;
import server.models.User;
import server.services.database.DataBase;
import server.services.proxy.AEProxyDB;
import server.services.proxy.ProxyDB;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class CommandServiceTest {

    @Mock
    private Authenticator authenticator;
    @Mock
    private Authorizator authorizator;
    @Mock
    private DataBase dataBase;

    private  ProxyDB proxyDB;
    private  CommandService aeCommandService;


    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
        proxyDB = new AEProxyDB(dataBase, authenticator, authorizator);
        aeCommandService = new AECommandService(proxyDB);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }



    @Test
    void testRegisterCommand() {
        User testUser = new AEUser("k","l","m","n","o");
        doNothing().when(authenticator).authenticate(isA(String.class), isA(String.class), isA(String.class), isA(String.class), isA(String.class));
        when(authenticator.authenticate("k")).thenReturn(testUser);
        String sessionID = proxyDB.register("k","l","m","n","o");
        assertDoesNotThrow(() -> aeCommandService.handleCommand("register --username k --password l --first-name m --last-name n --email o"));
        assertNotNull(sessionID);
    }
    @Test
    void testLoginCommandWithUsernameAndPassword() {
    }
    @Test
    void testLoginCommandWithSessionID() {
    }
    @Test
    void testUpdateUserCommand() {
    }
    @Test
    void testResetPasswordCommand() {
    }
    @Test
    void testLogoutCommand() {
    }
    @Test
    void testAddAdminUserCommand() {
    }
    @Test
    void testRemoveAdminUserCommand() {
    }
    @Test
    void testDeleteUserCommand() {
    }
}