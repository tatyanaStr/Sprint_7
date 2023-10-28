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
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

    LoginAndDelete del = new LoginAndDelete();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    private Response sendCreateCourierRequest(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    private Response sendLoginCourierRequest(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login");
    }

    @Test
    @DisplayName("Авторизация курьера с заполнением всех полей")
    public void loginCourierTest() {

        String value = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, null);

        sendCreateCourierRequest(courier);

        var response = sendLoginCourierRequest(courier);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);

        System.out.println(response.body().asString());

        del.deleteCourier(courier);
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным логином")
    public void loginCourierIncorrectLoginTest() {

        String value = RandomString.getAlphaNumericString(5);
        String incorrectLoginValue = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, null);
        Courier courierIncorrect = new Courier(incorrectLoginValue, value, null);

        sendCreateCourierRequest(courier);
        var response = sendLoginCourierRequest(courierIncorrect);
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        System.out.println(response.body().asString());

        del.deleteCourier(courier);
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным паролем")
    public void loginCourierIncorrectPasswordTest() {

        String value = RandomString.getAlphaNumericString(5);
        String incorrectPasswordValue = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, null);
        Courier courierIncorrect = new Courier(value, incorrectPasswordValue, null);

        sendCreateCourierRequest(courier);
        var response = sendLoginCourierRequest(courierIncorrect);
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        System.out.println(response.body().asString());

        del.deleteCourier(courier);
    }

    @Test
    @DisplayName("Авторизация курьера без ввода пароля")
    public void loginCourierWithoutValueTest() {

        String value = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, null);
        Courier courierEmpty = new Courier(value, "", null);

        sendCreateCourierRequest(courier);
        var response = sendLoginCourierRequest(courierEmpty);
        response.then().assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);

        System.out.println(response.body().asString());

        del.deleteCourier(courier);
    }

    @Test
    @DisplayName("Авторизация курьера с некорректными данными")
    public void loginCourierNotRegistrateTest() {

        String value = RandomString.getAlphaNumericString(5);
        Courier courier = new Courier(value, value, null);

        var response = sendLoginCourierRequest(courier);
        response.then().assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        System.out.println(response.body().asString());
    }
}
