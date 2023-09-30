package ru.praktikum_services.qa_scooter.test;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.json.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String[] COLOR;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    public CreateOrderTest(String[] COLOR) {
        this.COLOR = COLOR;
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][] {
                {new String[] {"BLACK", "GREY"}},
                {new String[] {"BLACK"}},
                {new String[] {"GREY"}},
                {new String[] {}}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами самокатов")
    public void createOrderColorTest() {

        var order = Order.createOrder(COLOR);

        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .post("/api/v1/orders");

        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);

        System.out.println(response.body().asString());
    }
}
