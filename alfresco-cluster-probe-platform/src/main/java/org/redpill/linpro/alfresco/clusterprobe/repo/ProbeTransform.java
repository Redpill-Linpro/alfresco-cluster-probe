package org.redpill.linpro.alfresco.clusterprobe.repo;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.filestore.FileContentReader;
import org.alfresco.repo.content.filestore.FileContentWriter;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.redpill.linpro.alfresco.clusterprobe.AbstractProbe;
import org.redpill.linpro.alfresco.clusterprobe.ProbeConfiguration;
import org.redpill.linpro.alfresco.clusterprobe.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author Marcus Svartmark - Redpill Linpro AB
 */
public class ProbeTransform extends AbstractProbe {

  private static final Logger LOG = Logger.getLogger(ProbeTransform.class);


  private ProbeConfiguration probeConfiguration;


  protected ContentService contentService;

  protected Pair<String, String> parseRequestedTransformation(final WebScriptRequest req) {
    Map<String, String> templateVars = req.getServiceMatch().getTemplateVars();
    String sourceExtension = templateVars.get("sourceExtension");
    String targetExtension = templateVars.get("targetExtension");
    return new Pair<>(sourceExtension, targetExtension);
  }

  protected void testTransform(String sourceExtension, String targetExtension) {
    if ("docx".equalsIgnoreCase(sourceExtension) && "pdf".equalsIgnoreCase(targetExtension)) {
      transformDocxToPdf();
    } else if ("docx".equalsIgnoreCase(sourceExtension) && "png".equalsIgnoreCase(targetExtension)) {
      transformDocxToPng();
    } else {
      throw new UnsupportedOperationException("Test transformation from " + sourceExtension + " to " + targetExtension + " not supported");
    }
  }

  protected void transformDocxToPdf() {

    AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<Void>() {

      @Override
      public Void doWork() throws Exception {
        InputStream resourceAsStream = null;
        OutputStream outStream = null;
        File createTempFile = File.createTempFile("test" + System.currentTimeMillis(), ".docx");
        resourceAsStream = this.getClass().getResourceAsStream("/test.docx");
        if (resourceAsStream == null) {
          throw new AlfrescoRuntimeException("Could not find test.docx to test transformer on");
        }
        FileUtils.copyInputStreamToFile(resourceAsStream, createTempFile);

        FileContentReader contentReader = new FileContentReader(createTempFile);
        contentReader.setMimetype(MimetypeMap.MIMETYPE_OPENXML_WORDPROCESSING);
        FileContentWriter contentWriter;
        contentWriter = new FileContentWriter(File.createTempFile("test" + System.currentTimeMillis(), ".pdf"));
        contentWriter.setMimetype(MimetypeMap.MIMETYPE_PDF);

        contentService.transform(contentReader, contentWriter);
        return null;
      }
    }
    );
  }

  protected void transformDocxToPng() {

    AuthenticationUtil.runAsSystem(new AuthenticationUtil.RunAsWork<Void>() {

      @Override
      public Void doWork() throws Exception {
        InputStream resourceAsStream = null;
        OutputStream outStream = null;
        File createTempFile = File.createTempFile("test" + System.currentTimeMillis(), ".docx");
        resourceAsStream = this.getClass().getResourceAsStream("/test.docx");
        if (resourceAsStream == null) {
          throw new AlfrescoRuntimeException("Could not find test.docx to test transformer on");
        }
        FileUtils.copyInputStreamToFile(resourceAsStream, createTempFile);

        FileContentReader contentReader = new FileContentReader(createTempFile);
        contentReader.setMimetype(MimetypeMap.MIMETYPE_OPENXML_WORDPROCESSING);
        FileContentWriter contentWriter;
        contentWriter = new FileContentWriter(File.createTempFile("test" + System.currentTimeMillis(), ".png"));
        contentWriter.setMimetype(MimetypeMap.MIMETYPE_IMAGE_PNG);

        contentService.transform(contentReader, contentWriter);
        return null;
      }
    }
    );
  }

  @Override
  protected Settings getProbeSettings(final WebScriptRequest req) {
    final String server = getServer();
    checkConfiguredServer(server);
    final Settings settings = new Settings("Transformation is working on server " + server, 200);
    Pair<String, String> parseRequestedTransformation = parseRequestedTransformation(req);
    try {
      testTransform(parseRequestedTransformation.getFirst(), parseRequestedTransformation.getSecond());
    } catch (Exception e) {
      //Set code to 500 on
      settings.text = "Transformation not available. An occured error on server " + server + ": " + e.getMessage();
      settings.code = 500;
      LOG.error(settings.text, e);
    }

    return settings;
  }


  @Override
  protected String getConfiguredServer() {
    return probeConfiguration.getProbeHost();
  }

  public void setProbeConfiguration(ProbeConfiguration probeConfiguration) {
    this.probeConfiguration = probeConfiguration;
  }

  public void setContentService(ContentService contentService) {
    this.contentService = contentService;
  }
  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    Assert.notNull(probeConfiguration, "probeConfiguration is null");
    Assert.notNull(contentService, "contentService is null");
  }
}
