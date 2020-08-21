package org.redpill.linpro.alfresco;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.redpill.alfresco.test.AbstractWebScriptIT;

import static io.restassured.RestAssured.given;

public class PostSettingsIT extends AbstractWebScriptIT {

    @Test
    public void testPostSettingOK(){
        Response response = given()
                .baseUri(getBaseUri())
                .expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .when()
                .get("/org/redpill/alfresco/clusterprobe/settings");

        boolean boolean1 = response.getBody().jsonPath().getBoolean("result[0].repo");

        given().
                baseUri(getBaseUri())
                .body("{\"server\":\"localhost.home.se\",\"type\":\"repo\",\"value\":" + !boolean1 + "}")
                .expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .when()
                .post("/org/redpill/alfresco/clusterprobe/settings");

        Response response2 = given()
                .baseUri(getBaseUri())
                .expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .when()
                .get("/org/redpill/alfresco/clusterprobe/settings");

        boolean boolean2 = response2.getBody().jsonPath().getBoolean("result[0].repo");
        Assert.assertNotEquals(boolean1, boolean2);
    }

    @Test
    public void testPostUnknownHost() {
        given().
                baseUri(getBaseUri())
                .body("{\"server\":\"unkownHost\",\"type\":\"repo\",\"value\":true}")
                .expect()
                .statusCode(400)
                .when()
                .post("/org/redpill/alfresco/clusterprobe/settings");
    }
    @Test
    public void testPostNoHost() {
        given().
                baseUri(getBaseUri())
                .body("{\"type\":\"repo\",\"value\":true}")
                .expect()
                .statusCode(400)
                .when()
                .post("/org/redpill/alfresco/clusterprobe/settings");
    }
    @Test
    public void testPostNoType() {
        given().
                baseUri(getBaseUri())
                .body("{\"server\":\"localhost.home.se\",\"value\":true}")
                .expect()
                .statusCode(400)
                .when()
                .post("/org/redpill/alfresco/clusterprobe/settings");
    }

    @Test
    public void testPostUnknownValue() {
        given().
                baseUri(getBaseUri())
                .body("{\"server\":\"localhost.home.se\",\"type\":\"repo\",\"value\":\"unkownValue\"}")
                .expect()
                .statusCode(500)
                .when()
                .post("/org/redpill/alfresco/clusterprobe/settings");
    }
}
