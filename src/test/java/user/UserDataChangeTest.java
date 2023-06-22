package user;

import burgers.user.Token;
import burgers.user.User;
import burgers.user.UserClient;
import burgers.user.UserCredentials;
import com.google.gson.JsonObject;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDataChangeTest {
    private UserClient userClient;
    private UserClient userClient_delete;
    private Token token;

    @Before
    public void setup() {
        userClient = new UserClient();
        userClient_delete = new UserClient();
    }

    @After
    public void teardown() {
        userClient_delete.delete(token);
    }

    @Test
    @DisplayName("Проверка успешного изменения почты пользовтаеля")
    public void userChangeEmailSuccess() {
        String message = "При попытке изменения почты пользователя получен некорректный ответ";
        User user = User.getRandom();
        String expectedNewEmail = (RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@yandex.ru");
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        token = userClient.login(creds);
        String actualNewEmail = userClient.changeUserEmail(token, expectedNewEmail);
        assertEquals(message, expectedNewEmail, actualNewEmail);
    }

    @Test
    @DisplayName("Проверка успешного изменения имени пользовтаеля")
    public void userChangeNameSuccess() {
        String message = "При попытке изменения имени пользователя получен некорректный ответ";
        User user = User.getRandom();
        String expectedNewName = RandomStringUtils.randomAlphabetic(10);
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        token = userClient.login(creds);
        String actualNewName = userClient.changeUserName(token, expectedNewName);
        assertEquals(message, expectedNewName, actualNewName);
    }

    @Test
    @DisplayName("Проверка успешного изменения пароля пользовтаеля")
    public void userChangePasswordSuccess() {
        String message = "При попытке изменения имени пароля получен некорректный ответ";
        User user = User.getRandom();
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        token = userClient.login(creds);
        userClient.changeUserPassword(token, newPassword);
        creds.setPassword(newPassword);
        userClient.logout(token);
        token = userClient.login(creds);
        assertNotNull(message, token.getAccessToken());
        assertNotNull(message, token.getRefreshToken());
    }

    @Test
    @DisplayName("Проверка неуспешного изменения почты неавторизованного пользователя")
    public void userChangeEmailRejection() {
        String message = "При попытке изменения почты неавторизованного пользователя получен некорректный ответ";
        String expectedMessage = "You should be authorised";
        User user = User.getRandom();
        var jsonWithNewEmail = new JsonObject();
        jsonWithNewEmail.addProperty("email", RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@yandex.ru");
        token = userClient.create(user);
        String expectedOldEmail = user.getEmail();
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        String actualMessage = userClient.changeUserRejection(jsonWithNewEmail.toString());
        token = userClient.login(creds);
        assertEquals(message, expectedMessage, actualMessage);
        assertEquals(message, expectedOldEmail, userClient.getUserDataEmail(token));
    }

    @Test
    @DisplayName("Проверка неуспешного изменения имени неавторизованного пользователя")
    public void userChangeNameRejection() {
        String message = "При попытке изменения имени неавторизованного пользователя получен некорректный ответ";
        String expectedMessage = "You should be authorised";
        User user = User.getRandom();
        var jsonWithNewName = new JsonObject();
        jsonWithNewName.addProperty("name", RandomStringUtils.randomAlphabetic(10));
        token = userClient.create(user);
        String expectedOldName = user.getName();
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        String actualMessage = userClient.changeUserRejection(jsonWithNewName.toString());
        token = userClient.login(creds);
        assertEquals(message, expectedMessage, actualMessage);
        assertEquals(message, expectedOldName, userClient.getUserDataName(token));
    }

    @Test
    @DisplayName("Проверка неуспешного изменения пароля неавторизованного пользователя")
    public void userChangePasswordRejection() {
        String message = "При попытке изменения пароля неавторизованного пользователя получен некорректный ответ";
        String expectedMessage = "You should be authorised";
        User user = User.getRandom();
        var jsonWithNewPassword = new JsonObject();
        jsonWithNewPassword.addProperty("password", RandomStringUtils.randomAlphabetic(10));
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        String actualMessage = userClient.changeUserRejection(jsonWithNewPassword.toString());
        token = userClient.login(creds);
        assertEquals(message, expectedMessage, actualMessage);
        assertNotNull(message, token.getAccessToken());
        assertNotNull(message, token.getRefreshToken());
    }
}
