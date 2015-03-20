package com.excilys.cdb.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class HelloServlet.
 */
@WebServlet("/cdb/*")
public class CdbServlet extends HttpServlet {

    private static final long serialVersionUID = 8756374436015233990L;

    /* ***
     * ATTRIBUTES
     */
    private final Logger            mLogger;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public CdbServlet() {
        super();
        mLogger = LoggerFactory.getLogger(getClass());
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        final StringBuilder debugSb = new StringBuilder();
        if (action != null) {
            debugSb.append(action);
        }

        if (request.getQueryString() != null) {
            debugSb.append(request.getQueryString());
        }
        mLogger.debug("doGet: URL=" + debugSb.toString());

        if (action == null) {
            action = "/dash";
        }


        // handle dashboard
        if (action.equals("/dash")) {
            mGotoDashboard(request, response);
            return;
        }
    }

    private void mGotoDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        getServletContext().getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }

}
