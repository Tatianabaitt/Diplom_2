package user;

import burgers.user.User;
import burgers.user.UserClient;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreatUserWithoutRequiredParamsTest {
    private UserClient userClient;

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Проверка не успешной регистрации пользователя без почты")
    public void userRegistrationWithoutEmail() {
        String message = "При попытке создания пользователя без почты в теле ответа вернулось сообщение отлчиное от ожидаемого";
        String expected = "Email, password and name are required fields";
        User user = User.getRandomWithoutEmail("");
        String actual = userClient.createBadUser(user);
        assertEquals(message, expected, actual);
    }

    @Test
    @DisplayName("Проверка не успешной регистрации пользователя без пароля")
    public void userRegistrationWithoutPassword() {
        String message = "При попытке создания пользователя без пароля в теле ответа вернулось сообщение отлчиное от ожидаемого";
        String expected = "Email, password and name are required fields";
        User user = User.getRandomWithoutPassword("");
        String actual = userClient.createBadUser(user);
        assertEquals(message, expected, actual);
    }

    @Test
    @DisplayName("Проверка не успешной регистрации пользователя без имени")
    public void userRegistrationWithoutName() {
        String message = "При попытке создания пользователя без имени в теле ответа вернулось сообщение отлчиное от ожидаемого";
        String expected = "Email, password and name are required fields";
        User user = User.getRandomWithoutName("");
        String actual = userClient.createBadUser(user);
        assertEquals(message, expected, actual);
    }
}
