package org.example.lab3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class MyTestClass {
    private static final String baseUrl = "https://petstore.swagger.io/v2";
    private static final String STORE = "/store",
            ORDER = STORE + "/order",
            ORDER_ORDERID = ORDER + "/{orderId}";

    private final int testNumber = 49;
    private final String testWord = "Eva";

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = baseUrl;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyPostAction() {
        Map<String, ?> body = Map.of(
                "id", testNumber,
                "petId", testNumber,
                "quantity", testNumber,
                "shipDate", "2025-01-21T17:23:14.474Z",
                "status", "placed",
                "complete", true
        );

        given().body(body)
                .post(ORDER)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test(dependsOnMethods = "verifyPostAction")
    public void verifyGetAction() {
        given().pathParam("orderId", testNumber)
                .get(ORDER_ORDERID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("petId", equalTo(testNumber));
    }

    public class Tag{
        public int id;
        public String name;

        public Tag(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }


    @Test
    public void verifyPutAction() {
        Map<String, ?> body = Map.of(
                "id", testNumber,
                "category", Map.of(
                        "id", testNumber,
                        "name", testWord
                ),
                "name", testWord,
                "photoUrls", new String[0],
                "tags", new Tag[] {new Tag(testNumber, testWord)},
                "status", testWord
        );

        given().body(body)
                .put("/pet")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(testNumber));
    }
}
