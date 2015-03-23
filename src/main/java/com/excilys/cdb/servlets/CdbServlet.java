package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.dao.mysql.CompanyDao;
import com.excilys.cdb.dao.mysql.ComputerDao;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.IService;
import com.excilys.cdb.service.Service;

/**
 * Servlet implementation class HelloServlet.
 */
@WebServlet("/cdb/*")
public class CdbServlet extends HttpServlet {

    private static final long serialVersionUID = 8756374436015233990L;

    /* ***
     * CONSTANTS
     */
    /** uri to redirect to when accessing to '/'. */
    private static final String DASH_URL         = "/dashboard";
    private static final String COMPUTER_SEARCH  = "/searchComputer";
    private static final String ROOT_REDIRECT    = DASH_URL;

    private static final int    PAGE_LENGTH      = 10;


    /* ***
     * ATTRIBUTES
     */
    private final Logger            mLogger;


    private ICompanyDao         mCompanyDao;
    private IComputerDao        mComputerDao;
    private IService            mService;
    @Override
    public void init() {
        mCompanyDao = CompanyDao.getInstance();
        mComputerDao = ComputerDao.getInstance();
        mService = new Service(mComputerDao, mCompanyDao);
    }

    @Override
    public void destroy() {
        mCompanyDao = null;
        mComputerDao = null;
        mService = null;
    }


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

        mLogRequest(request, response);
        String action = request.getPathInfo();

        if (action == null) {
            action = ROOT_REDIRECT;
        }

        if (action.equals(DASH_URL)) {
            // handle dashboard
            mGotoDashboard(request, response);
            return;
        } else if (action.equals(COMPUTER_SEARCH)) {
            // handle dashboard search
            mGotoDashboard(request, response);
        }
    }

    /**
     * Redirect to dashBoard.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void mGotoDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        List<Computer> computers;
        // presence of a queryString that search for computer name?
        final String queryName = request.getParameter("search");

        // select all computer in page range
        if (queryName != null && !queryName.trim().isEmpty()) {
            computers = mService.listComputersLikeName(0, PAGE_LENGTH, queryName);
        } else {
            computers = mService.listComputersByName(0, PAGE_LENGTH);
        }


        // TODO count results
        final int totalResults = mService.getComputerCount();

        final Page<Computer> page = new Page<Computer>(computers, 1, 0, totalResults);

        request.setAttribute("resultsPage", page);
        getServletContext().getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }

    /* ***
     * PRIVATE TOOLS
     */
    /**
     * prints some logs of URL.
     *
     * @param request
     * @param response
     */
    private void mLogRequest(HttpServletRequest request, HttpServletResponse response) {
        final String action = request.getPathInfo();
        final StringBuilder debugSb = new StringBuilder();
        if (action != null) {
            debugSb.append(action);
        }

        if (request.getQueryString() != null) {
            debugSb.append(request.getQueryString());
        }
        mLogger.debug("doGet: URL=" + debugSb.toString());
    }


}
