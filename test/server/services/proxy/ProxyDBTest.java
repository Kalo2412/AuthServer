package server.services.proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.authenticator.Authenticator;
import server.authorizator.Authorizator;
import server.exceptions.UserExistsException;
import server.exceptions.UserNotFoundException;
import server.models.AEUser;
import server.models.User;
import server.services.database.DataBase;
import server.services.session.AESession;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProxyDBTest {
    @Mock
    private Authenticator authenticator;

    @Mock
    private Authorizator authorizator;

    @Mock
    private DataBase dataBase;

    @InjectMocks
    private AEProxyDB proxyDB;
    @Test
    void testRegisterFirstUser() {
        User testUser = new AEUser("kalo", "sduhkal", "dnaqlkjs", "snkaj", "sjl");
        doNothing().when(authenticator).authenticate(isA(String.class), isA(String.class), isA(String.class), isA(String.class), isA(String.class));
        when(authenticator.authenticate("kalo")).thenReturn(testUser);
        String test = proxyDB.register("kalo", "sduhkal", "dnaqlkjs", "snkaj", "sjl");
        AESession[] k = new AESession[1];
        User[] users = new AEUser[1];
        proxyDB.getLoggedUsers().keySet().toArray(k);
        proxyDB.getLoggedUsers().values().toArray(users);
        assertEquals(k[0].getUniqueID(), test);
        assertTrue(users[0].isAuthenticated());
        assertEquals(users[0].getUserData().username(), "kalo");
    }

    @Test
    void testRegisterUserWithTakenUsername() {
        User testUser = new AEUser("kalo", "sduhkal", "dnaqlkjs", "snkaj", "sjl");
        doThrow(UserExistsException.class).when(authenticator).authenticate("kalo", "sduhkal", "dnaqlkjs", "snkaj", "sjl");
        assertThrows(UserExistsException.class, () -> proxyDB.register("kalo", "sduhkal", "dnaqlkjs", "snkaj", "sjl"));
        assertEquals(proxyDB.getLoggedUsers().size(), 0);
    }

    @Test
    void testLoginWithUnknownUser() {
        doThrow(UserNotFoundException.class).when(authenticator).authenticate("kalo", "sduhkal");
        assertThrows(UserNotFoundException.class, () -> proxyDB.login("kalo", "sduhkal"));
        assertEquals(proxyDB.getLoggedUsers().size(), 0);
    }

    @Test
    void testLoginWithWrongPassword() {
        doThrow(UserNotFoundException.class).when(authenticator).authenticate("kalo", "sduhkal");
        assertThrows(UserNotFoundException.class, () -> proxyDB.login("kalo", "sduhkal"));
        assertEquals(proxyDB.getLoggedUsers().size(), 0);
    }

    @Test
    void testLoginSuccessful() {
        User testUser = new AEUser("kalo", "sduhkal", "dnaqlkjs", "snkaj", "sjl");
        when(authenticator.authenticate("kalo", "sduhkal")).thenReturn(testUser);
        proxyDB.login("kalo", "sduhkal");
        assertEquals(proxyDB.getLoggedUsers().size(), 1);
    }
}