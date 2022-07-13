package order;

import burgers.order.Ingredients;
import burgers.order.Order;
import burgers.order.OrderClient;
import burgers.user.Token;
import burgers.user.User;
import burgers.user.UserClient;
import burgers.user.UserCredentials;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;

public class GetOrdersUserTest {
    private UserClient userClient;
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
    @DisplayName("Проверка успешного получения перечня заказов авторизованного пользователя")
    public void getOrderForAuthUserSuccess() {
        String message = "При попытке получения заказов авторизованным пользователем что-то пошло не так";
        OrderClient orderClient1 = new OrderClient();
        OrderClient orderClient2 = new OrderClient();
        OrderClient orderClient3 = new OrderClient();
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(5));
        User user = User.getRandom();
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        Order order0 = orderClient1.createOrderWithAuthUser(token, ingredients);
        Order order1 = orderClient2.createOrderWithAuthUser(token, ingredients);
        Order order2 = orderClient3.createOrderWithAuthUser(token, ingredients);
        List<Integer> actualNumberOrders = orderClient.getOrderForAuthUser(token);
        assertEquals(message, actualNumberOrders.size(), 3);
        assertEquals(message, order0.getNumber(), actualNumberOrders.get(0).intValue());
        assertEquals(message, order1.getNumber(), actualNumberOrders.get(1).intValue());
        assertEquals(message, order2.getNumber(), actualNumberOrders.get(2).intValue());
    }

    @Test
    @DisplayName("Проверка неуспешного получения перечня заказов неавторизованного пользователя")
    public void getOrderForNotUserRejection() {
        String message = "При попытке получения заказов не авторизованным пользователем что-то пошло не так";
        String expectedMessage = "You should be authorised";
        OrderClient orderClient = new OrderClient();
        OrderClient orderClientNew = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(5));
        User user = User.getRandom();
        token = userClient.create(user);
        UserCredentials creds = UserCredentials.from(user);
        Order order = orderClient.createOrderWithAuthUser(token, ingredients);
        userClient.logout(token);
        String actualMessage = orderClientNew.getOrderForNotAuthUser();
        token = userClient.login(creds);
        assertEquals(message, actualMessage, expectedMessage);
    }
}
