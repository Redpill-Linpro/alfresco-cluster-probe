package org.redpill.linpro.alfresco;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.redpill.alfresco.test.AbstractWebScriptIT;

import static io.restassured.RestAssured.given;

public class GetSettingsIT extends AbstractWebScriptIT {

    @Test
    public void testGetSettingsOK() {

        Response response = given()
                .baseUri(getBaseUri())
                .expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .when()
                .get("/org/redpill/alfresco/clusterprobe/settings");

        response.prettyPrint();
    }
}
