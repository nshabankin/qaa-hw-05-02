package ru.netology.auth;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class AuthTest {

    // Спецификация запросов
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    // Сгенерированные тестовые пользователи, статусы вводятся вручную
    static UserGenerator.UserInfo activeUser = UserGenerator.Registration.generateUser("active");
    static UserGenerator.UserInfo blockedUser = UserGenerator.Registration.generateUser("blocked");

    // Измененные данные активного пользователя для проверки неправильного логина или пароля
    UserGenerator.UserInfo invalidLoginUser = new UserGenerator.UserInfo("invalidLogin", activeUser.getPassword(), "active");
    UserGenerator.UserInfo invalidPasswordUser = new UserGenerator.UserInfo(activeUser.getLogin(), "invalidPassword", "active");

    // Метод внесения пользователя в систему
    static void registerUser(UserGenerator.UserInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @BeforeAll
    static void setUpAll() {
        // Внесение пользователей в систему
        registerUser(activeUser);
        registerUser(blockedUser);
    }

    @Test
    void shouldAuthorizeActiveUser() {
        given()
                .spec(requestSpec)
                .body(activeUser)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAuthorizeBlockedUser() {
        given()
                .spec(requestSpec)
                .body(blockedUser)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotAuthorizeWithInvalidLogin() {
        given()
                .spec(requestSpec)
                .body(invalidLoginUser)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotAuthorizeWithInvalidPassword() {
        given()
                .spec(requestSpec)
                .body(invalidPasswordUser)
                .when()
                .post("/api/auth")
                .then()
                .statusCode(400);
    }
}