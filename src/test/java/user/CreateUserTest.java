package user;

import burgers.user.Token;
import burgers.user.User;
import burgers.user.UserClient;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateUserTest {

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
    @DisplayName("Проверка успешной регистрации пользователя с корректным логином, паролем и именем")
    public void userRegistrationSuccess() {
        String messege = "При попытке регистрации пользователя в теле ответа получен некорретный токен";
        User user = User.getRandom();
        token = userClient.create(user);
        assertNotNull(messege, token.getAccessToken());
        assertNotNull(messege, token.getRefreshToken());
    }

    @Test
    @DisplayName("Проверка не успешной регистрации дубликата пользователя")
    public void userRegistrationDublicate() {
        String messege = "При попытке создания дубликата пользователя в теле ответа вернулось сообщение отлчиное от ожидаемого";
        String expected = "User already exists";
        User user = User.getRandom();
        token = userClient.create(user);
        String actual = userClient.createBadUser(user);
        assertEquals(messege, expected, actual);
    }
}
