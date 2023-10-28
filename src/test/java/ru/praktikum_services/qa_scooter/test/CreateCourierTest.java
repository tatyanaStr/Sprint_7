package ru.praktikum_services.qa_scooter.test;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.additional_class.LoginAndDelete;
import ru.praktikum_services.qa_scooter.json.Courier;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum_services.qa_scooter.additional_class.RandomString;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;


public class CreateCourierTest {

    LoginAndDelete del = new LoginAndDelete();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    private Response sendRequest(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Создание курьера со всеми полями")
    public void createCourierTest() {

        String value = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, value);

        var response = sendRequest(courier);
        response.then().assertThat().body("ok", is(true))
                .and()
                .statusCode(201);

        System.out.println(response.body().asString());

        del.deleteCourier(courier);
    }

    @Test
    @DisplayName("Создание курьера с одинаковым логином")
    public void createCourierWithSameLoginTest() {

        String value = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, value);

        sendRequest(courier);
        var response = sendRequest(courier);
        response.then().assertThat().body("message", is("Этот логин уже используется"))
                .and()
                .statusCode(409);

        System.out.println(response.body().asString());

        del.deleteCourier(courier);
    }

    @Test
    @DisplayName("Создание курьера без ввода пароля")
    public void createCourierWithoutValueTest() {

        String value = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, "", "");

        var response = sendRequest(courier);
        response.then().assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        System.out.println(response.body().asString());
    }
}