package org.redpill.alfresco.clusterprobe;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;

public abstract class AbstractProbe extends AbstractWebScript {

  protected static final String DEFAULT_SERVER = "localhost";

  @Override
  public void execute(final WebScriptRequest req, final WebScriptResponse res) throws IOException {
    try {
      final Settings settings = getProbeSettings();

      final int code = settings.code;
      String text = settings.text;


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
    } finally {
      invalidateSession(req);
    }
  }

  private void invalidateSession(WebScriptRequest req) {
    if (!(req instanceof WebScriptServletRequest)) {
      return;
    }

    WebScriptServletRequest request = (WebScriptServletRequest) req;

    HttpServletRequest servletRequest = request.getHttpServletRequest();

    HttpSession session = servletRequest.getSession(false);
    
    if (session == null) {
      return;
    }
    
    session.invalidate();
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
