package org.redpill.linpro.alfresco.clusterprobe;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProbe extends DeclarativeWebScript {

    protected static final String DEFAULT_SERVER = "localhost";

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        try {

            final Settings settings = getProbeSettings(req);

            final int code = settings.code;
            String text = settings.text;
            Map<String, Object> model = new HashMap<>();
            model.put("text", text);
            status.setCode(code);
            return model;

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

    protected Settings getProbeSettings() {
        return getProbeSettings(null);
    }

    protected abstract Settings getProbeSettings(final WebScriptRequest req);

}
