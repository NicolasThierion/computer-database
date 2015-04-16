package com.excilys.cdb.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.service.IComputerService;

public final class ServletUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServletUtils.class);
    private static final String SPRING_CONFIG_XML = "spring-config.xml";

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

    public static IComputerService initComputerService() {
        final ApplicationContext context = new ClassPathXmlApplicationContext(SPRING_CONFIG_XML);
        final IComputerService computerService = (IComputerService) context.getBean("computerService");
        return computerService;
    }

}
