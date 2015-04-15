package org.redpill.alfresco.clusterprobe.repo.ft;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redpill.alfresco.test.AbstractRepoFunctionalTest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;

public class SettingsPostFunctionalTest extends AbstractRepoFunctionalTest {
  
  @Before
  public void setUp() {
    RestAssured.defaultParser = Parser.JSON;
    RestAssured.authentication = preemptive().basic("admin", "admin");
    // RestAssured.proxy("localhost", 8888);
  }

  @After
  public void tearDown() {
    RestAssured.reset();
  }

  @Test
  public void testSettingsPost() throws JSONException {
    RestAssured.requestContentType(ContentType.JSON);
    RestAssured.responseContentType(ContentType.JSON);
    
    String server = "foobar-server-" + System.currentTimeMillis();
    JSONObject json = new JSONObject();
    json.put("server", server);
    json.put("code", 432);
    json.put("text", server + "-ONLINE");
    
    given()
        .baseUri(getBaseUri())
        .request().body(json.toString())
        .expect().statusCode(200)
        .when().post("/org/redpill/alfresco/clusterprobe/settings");
    
    given()
        .baseUri(getBaseUri())
        .pathParam("server", server)
        .expect().statusCode(200)
        .expect()
          .body("text", equalTo(server + "-ONLINE"))
          .body("code", equalTo(432))
        .when().get("/org/redpill/alfresco/clusterprobe/settings?server={server}");
  }
  
}
