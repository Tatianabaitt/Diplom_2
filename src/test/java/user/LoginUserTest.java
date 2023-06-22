package user;

import burgers.user.Token;
import burgers.user.User;
import burgers.user.UserClient;
import burgers.user.UserCredentials;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginUserTest {
    private static UserClient userClient;
    private Token token;

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Проверка успешной авторизации пользователя с корректным логином и паролем")
    public void userLoginSuccess() {
        String message = "При попытке логине пользователя в теле ответа получен некорретный токен";
        User user = User.getRandom();
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        userClient.logout(token);
        token = userClient.login(creds);
        assertNotNull(message, token.getAccessToken());
        assertNotNull(message, token.getRefreshToken());
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации пользователя с некорректным email")
    public void userLoginWithBadEmail() {
        String message = "При попытке логине пользователя с неправильной почтой в теле ответа получено некоррекртное сообщение";
        String expected = "email or password are incorrect";
        User user = User.getRandom();
        token = userClient.create(user);
        userClient.logout(token);
        UserCredentials creds = UserCredentials.from(user);
        creds.UserCredentialsBadEmail(creds);
        String actual = userClient.loginWithBadParams(creds);
        assertEquals(message, expected, actual);
    }

    @Test
    @DisplayName("Проверка неуспешной авторизации пользователя с некорректным паролем")
    public void userLoginWithBadPassword() {
        String message = "При попытке логине пользователя с неправильной паролем в теле ответа получено некоррекртное сообщение";
        String expected = "email or password are incorrect";
        User user = User.getRandom();
        token = userClient.create(user);
        userClient.logout(token);
        UserCredentials creds = UserCredentials.from(user);
        creds.UserCredentialsBadPassword(creds);
        String actual = userClient.loginWithBadParams(creds);
        assertEquals(message, expected, actual);

    }
}
