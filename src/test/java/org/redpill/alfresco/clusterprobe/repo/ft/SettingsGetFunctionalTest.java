package org.redpill.alfresco.clusterprobe.repo.ft;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redpill.alfresco.test.AbstractRepoFunctionalTest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;

public class SettingsGetFunctionalTest extends AbstractRepoFunctionalTest {
  
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
  public void testSettingsGet() {
    RestAssured.requestContentType(ContentType.JSON);
    RestAssured.responseContentType(ContentType.JSON);
    
    String server = "foobar-server";
    
    given()
        .baseUri(getBaseUri())
        .pathParam("server", server)
        .expect().statusCode(200)
        .expect()
          .body("text", equalTo(server + "-ONLINE"))
          .body("code", equalTo(200))
        .when().get("/org/redpill/alfresco/clusterprobe/settings?server={server}");
  }
  
}
