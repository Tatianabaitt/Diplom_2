package burgers;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestAssuredBurger {
    protected String URL = "https://stellarburgers.nomoreparties.site/api/";
    public final RequestSpecification reqSpec = given()
            .header("Content-type", "application/json")
            .baseUri(URL);
}
