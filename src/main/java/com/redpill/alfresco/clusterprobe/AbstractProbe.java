package com.redpill.alfresco.clusterprobe;

import java.io.IOException;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

public abstract class AbstractProbe extends AbstractWebScript {

  protected static final String DEFAULT_SERVER = "localhost";

  @Override
  public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {
    try {
      final Settings settings = getProbeSettings();

      final int code = settings.code;
      String text = settings.text;

      if (code > 399) {
        text = "";
      }

      res.addHeader("Content-Length", String.valueOf(text.length()));
      res.addHeader("Cache-Control", "no-cache");
      res.addHeader("Pragma", "no-cache");
      res.setContentType("text/plain");
      res.setContentEncoding("UTF-8");
      res.setStatus(code);
      res.getWriter().write(text);

      res.getWriter().flush();
      res.getWriter().close();
    } catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  protected String getServer() {
    String server = getConfiguredServer();

    if (server == null || server.length() == 0) {
      server = DEFAULT_SERVER;
    }

    return server;
  }

  protected abstract String getConfiguredServer();

  protected abstract Settings getProbeSettings();

}
