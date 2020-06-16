package org.redpill.linpro.alfresco;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.redpill.alfresco.test.AbstractWebScriptIT;

import static io.restassured.RestAssured.given;

public class ProbeShareIT extends AbstractWebScriptIT {
    private final String host = "localhost";
    @Test
    public void probeShareOK() {

        setShareStatus(true);

        Response response = given().
                baseUri(getBaseUri())
                .pathParam("hostName", host)
                .expect()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .when()
                .get("/org/redpill/alfresco/clusterprobe/probe/share/{hostName}");

        Assert.assertEquals(host + "-ONLINE", response.getBody().print());

        setShareStatus(false);

        Response response2= given().
                baseUri(getBaseUri())
                .pathParam("hostName", host)
                .expect()
                .statusCode(404)
                .contentType(ContentType.TEXT)
                .when()
                .get("/org/redpill/alfresco/clusterprobe/probe/share/{hostName}");

        Assert.assertEquals(host + "-OFFLINE", response2.getBody().print());

        setShareStatus(true);
    }
    @Test
    public void testProbeUnknownHost() {
        Response response = given().
                baseUri(getBaseUri())
                .pathParam("hostName", "unKnownHost")
                .expect()
                .statusCode(500)
                .when()
                .get("/org/redpill/alfresco/clusterprobe/probe/share/{hostName}");
    }
    private void setShareStatus(boolean status) {

        given().
                baseUri(getBaseUri())
                .body("{\"server\":\""+ host + "\",\"type\":\"share\",\"value\":" + status + "}")
                .expect()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .when()
                .post("/org/redpill/alfresco/clusterprobe/settings");
    }
}
