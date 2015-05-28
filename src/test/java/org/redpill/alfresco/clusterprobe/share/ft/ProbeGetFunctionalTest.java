package org.redpill.alfresco.clusterprobe.share.ft;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.preemptive;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redpill.alfresco.test.AbstractRepoFunctionalTest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;

public class ProbeGetFunctionalTest extends AbstractRepoFunctionalTest {
  
  private final static Logger LOG = Logger.getLogger(ProbeGetFunctionalTest.class);
  
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
    RestAssured.responseContentType("text/plain;charset=UTF-8");
    
    Response response = given()
        .baseUri(getBaseUri())
        .expect().statusCode(200)
        .body(equalTo("alfresco.private-ONLINE"))
        .when().get("/org/redpill/alfresco/clusterprobe/probe");
    
    if (LOG.isDebugEnabled()) {
      response.prettyPrint();
    }
  }
  
}
