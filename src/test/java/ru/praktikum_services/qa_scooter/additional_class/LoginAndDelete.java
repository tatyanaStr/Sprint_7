package ru.praktikum_services.qa_scooter.additional_class;

import io.restassured.response.Response;
import ru.praktikum_services.qa_scooter.json.Courier;
import ru.praktikum_services.qa_scooter.json.LoginResponse;

import static io.restassured.RestAssured.given;

public class LoginAndDelete {

    public void deleteCourier(Courier courier){
        Response response =  given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login");

        var id = response.body().as(LoginResponse.class).getId();

        Response resp =  given()
                .body(courier)
                .delete("/api/v1/courier/" + id);

        System.out.println(resp.body().asString());
    }
}
