package ru.praktikum_services.qa_scooter.test;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrderListTest() {

        Response response = given().get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue());

        System.out.println(response.body().asString());
    }
}
