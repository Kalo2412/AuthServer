package server.services.database;

import org.junit.jupiter.api.*;
import server.exceptions.PasswordTheSameAsOldException;
import server.exceptions.UserExistsException;
import server.exceptions.UserNotFoundException;
import server.models.AEUser;
import server.models.User;
import server.models.UserData;
import server.services.providers.AEPasswordSHA;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {
    private static final User testUser = new AEUser("kaloyan", "123456789", "Kaloyan", "Vachkov", "kaloyan.va@abv.bg");
    private static DataBase dataBase;
    @BeforeAll
    static void setUp() {
        dataBase = AEDataBase.getDataBase();
        dataBase.register(testUser);
    }
    @AfterAll
    static void teardown() {
        dataBase = null;
    }
    @Test
    void testDeleteUserWhoExists() {
        assertDoesNotThrow(() -> dataBase.delete(testUser));
    }

    @Test
    void testDeleteUserWhoDoesNotExist() {
        User newUser = new AEUser("nov", "12342342156789", "edi", "koisi", "test@gmail.com");
        assertThrows(NullPointerException.class,() -> dataBase.delete(newUser));
    }

    @Test
    void testUpdateUserData() {
        dataBase.update(testUser, new UserData("kalo", null, "novo", "ime", "emailche"));
        assertEquals(testUser.getUserData().username(), "kalo");
        assertEquals(testUser.getUserData().password(), "ed92e4c7e54e3f4a29d8041ec93124bd3c1ec4825701cac42b3fffaf0bd7120a");
        assertEquals(testUser.getUserData().firstName(), "novo");
        assertEquals(testUser.getUserData().lastName(), "ime");
        assertEquals(testUser.getUserData().email(), "emailche");
    }

    @Test
    void testUpdateUserPasswordWithSameAsOld() {
        assertThrows(PasswordTheSameAsOldException.class,() -> dataBase.updatePassword(testUser, "123456789", "123456789"));
    }

    @Test
    void testUpdateUserPasswordWithDifferentThanOld() {
        assertDoesNotThrow(() -> dataBase.updatePassword(testUser, "123456789", "7238491087"));
        assertEquals(testUser.getUserData().password(), AEPasswordSHA.hashPassword("7238491087"));
    }

    @Test
    void testRegisterNewUser() {
        User newUser = new AEUser("nov", "12342342156789", "edi", "koisi", "test@gmail.com");
        assertDoesNotThrow(() -> dataBase.register(newUser));
    }

    @Test
    void testRegisterSameUserTwoTimes() {
        assertThrows(UserExistsException.class, () -> dataBase.register(testUser));
    }

    @Test
    void testFindUserInDataBaseWithUsername() {
        assertDoesNotThrow(() -> dataBase.findUser(testUser.getUserData().username()));
        assertThrows(UserNotFoundException.class, () -> dataBase.findUser("random username"));
    }

    @Test
    void testFindUserInDataBaseWithUsernameAndPassword() {
        assertDoesNotThrow(() -> dataBase.findUser(testUser.getUserData().username(),
                testUser.getUserData().password()));
        assertThrows(UserNotFoundException.class, () -> dataBase.findUser("random username",
                "random password"));
    }

    @Test
    void testUpdateAndGetRights() {
        dataBase.updateRights(testUser, true);
        assertTrue(dataBase.getRights(testUser));
    }
}