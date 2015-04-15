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

public class ProbeGetFunctionalTest extends AbstractRepoFunctionalTest {
  
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
  public void testProbeGet() {
    RestAssured.requestContentType(ContentType.JSON);
    RestAssured.responseContentType("text/plain;charset=UTF-8");
    
    given()
        .baseUri(getBaseUri())
        .expect().statusCode(200)
        .body(equalTo("alfresco.private-ONLINE"))
        .when().get("/org/redpill/alfresco/clusterprobe/probe");
  }
  
}
