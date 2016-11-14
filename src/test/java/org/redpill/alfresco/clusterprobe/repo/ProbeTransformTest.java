package org.redpill.alfresco.clusterprobe.repo;

import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.util.Pair;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.redpill.alfresco.clusterprobe.Settings;
import org.redpill.alfresco.clusterprobe.repo.ProbeTransformTest.ProbeTransformMock;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 *
 * @author mars
 */
public class ProbeTransformTest {

  Mockery mock;

  ContentService localContentService;

  @Before
  public void setUp() {
    mock = new JUnit4Mockery();
    localContentService = mock.mock(ContentService.class);
  }

  @Test
  public void test() {

    mock.checking(new Expectations() {
      {
        oneOf(localContentService).transform(with(any(ContentReader.class)), with(any(ContentWriter.class)));
      }
    });
    ProbeTransformMock pt = new ProbeTransformMock();
    pt.getProbeSettings();
    mock.assertIsSatisfied();

  }

  public class ProbeTransformMock extends ProbeTransform {

    public ProbeTransformMock() {
      contentService = localContentService;
    }
    
    @Override
    public Pair<String, String> parseRequestedTransformation(final WebScriptRequest req) {
      return new Pair("docx","pdf");
    }

    @Override
    public String getConfiguredServer() {
      return "";
    }

    @Override
    public Settings getProbeSettings() {
      return super.getProbeSettings();
    }
  }
}
