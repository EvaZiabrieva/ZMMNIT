package org.example.lab4;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.testng.annotations.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Lab4_test {
    private static WireMockServer wireMockServer;

    @BeforeClass
    public static void setupMockServer() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8080));
        wireMockServer.start();

        WireMock.configureFor("localhost", 8080);
        WireMock.stubFor(WireMock.get("/success")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"Mariiya Larykova\"}")));
        WireMock.stubFor(WireMock.post("/postMock")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"result\": \"'Nothing' was created\"}")));
        WireMock.stubFor(WireMock.put("/putMock")
                .willReturn(WireMock.aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"\", \"surname\": \"\", \"message\": \"Internal Server Error\"}")));
        WireMock.stubFor(WireMock.delete("/deleteMock")
                .willReturn(WireMock.aResponse()
                        .withStatus(204))); // No Content

        RestAssured.baseURI = "http://localhost:8080";
        System.out.println("Мок-сервер запущено на localhost:8080");
    }

    @Test
    public void testGetRequest() {
        given()
                .when()
                .get("/success")
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("name", equalTo("Mariiya Larykova"));
    }

    @Test(dependsOnMethods = "testGetRequest")
    public void testPostRequest() {
        given()
                .when()
                .post("/postMock")
                .then()
                .statusCode(200)
                .body("result", equalTo("'Nothing' was created"));
    }

    @Test(dependsOnMethods = "testPostRequest")
    public void testPutRequest() {
        given()
                .when()
                .put("/putMock")
                .then()
                .statusCode(500)
                .body("name", equalTo(""))
                .body("surname", equalTo(""))
                .body("message", equalTo("Internal Server Error"));
    }

    @Test(dependsOnMethods = "testPutRequest")
    public void testDeleteRequest() {
        given()
                .when()
                .delete("/deleteMock")
                .then()
                .statusCode(204); // No Content
    }

    @AfterClass
    public static void stopMockServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            System.out.println("Мок-сервер зупинено");
        }
    }
}
