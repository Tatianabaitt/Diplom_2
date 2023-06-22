package burgers.order;

import burgers.RestAssuredBurger;
import burgers.user.Token;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderClient extends RestAssuredBurger {

    private final String ORDER_URL = "/orders";
    private final String INGREDIENTS_URL = "/ingredients";

    public List<String> getListIngredients(int countIngredients) {
        Random rand = new Random();
        List<String> allIdIngredients = reqSpec
                .when()
                .get(INGREDIENTS_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("data._id");
        List<String> ingredientsForBurger = new ArrayList<>();
        for (int i = 0; i < countIngredients; i++) {
            ingredientsForBurger.add(allIdIngredients.get(rand.nextInt(allIdIngredients.size())));
        }
        return ingredientsForBurger;
    }

    public Order createOrderWithAuthUser(Token token, Ingredients ingredientsForBurger) {
        var response = reqSpec
                .header("authorization", token.getAccessToken())
                .body(ingredientsForBurger)
                .when()
                .post(ORDER_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
        JsonObject element = new Gson().fromJson(response, JsonObject.class).getAsJsonObject("order");
        return new Gson().fromJson(element, Order.class);
    }

    public Order createOrderWihtoutAuthUser(Ingredients ingredientsForBurger) {
        var response = reqSpec
                .body(ingredientsForBurger)
                .when()
                .post(ORDER_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
        JsonObject element = new Gson().fromJson(response, JsonObject.class).getAsJsonObject("order");
        return new Gson().fromJson(element, Order.class);
    }

    public String createOrderWithoutIngredients(Token token) {
        return reqSpec
                .header("authorization", token.getAccessToken())
                .when()
                .post(ORDER_URL)
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");
    }

    public void createOrderWitBadHashIngredients(Token token, Ingredients ingredientsForBurger) {
        reqSpec
                .header("authorization", token.getAccessToken())
                .body(ingredientsForBurger)
                .when()
                .post(ORDER_URL)
                .then()
                .assertThat()
                .statusCode(500);
    }

    public List<Integer> getOrderForAuthUser(Token token) {
        return reqSpec
                .header("authorization", token.getAccessToken())
                .when()
                .get(ORDER_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("orders.number");
    }

    public String getOrderForNotAuthUser() {
        return reqSpec
                .when()
                .get(ORDER_URL)
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
    }
}
