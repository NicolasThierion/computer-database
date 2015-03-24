package com.excilys.cdb.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServletUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServletUtils.class);

    private ServletUtils() {
    }
    /**
     * prints some logs (IE : requested URL, ...).
     *
     * @param request
     * @param response
     */
    public static void logRequest(HttpServletRequest request, HttpServletResponse response) {
        final String action = request.getPathInfo();
        final StringBuilder debugSb = new StringBuilder();
        if (action != null) {
            debugSb.append(action);
        }

        if (request.getQueryString() != null) {
            debugSb.append(request.getQueryString());
        }
        LOGGER.debug("doGet: URL=" + debugSb.toString());
    }

}
