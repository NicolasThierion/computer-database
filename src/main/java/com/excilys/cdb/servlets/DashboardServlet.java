package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 8756374436015233990L;

    /* ***
     * CONSTANTS
     */

    private static final int    PAGE_LENGTH      = 10;
    private static final String DASH_URN         = "/WEB-INF/views/dashboard.jsp";

    /* ***
     * ATTRIBUTES
     */
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
    public DashboardServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletUtils.logRequest(request, response);
        final String action = request.getPathInfo();

        if (action != null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
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
        String queryName = request.getParameter("search");

        // search empty name if name is null => search for all computers.
        if (queryName == null) {
            queryName = "";
        }

        // select all computer in page range
        computers = mService.listComputersLikeName(0, PAGE_LENGTH, queryName);

        // count results & store them in a Page<Computer>
        final int totalResults = mService.getComputersCount(queryName);
        // TODO varying offset
        final Page<Computer> page = new Page<Computer>(computers, 1, 0, totalResults, queryName);

        // set result page & send redirect.
        request.setAttribute("resultsPageBean", page);
        getServletContext().getRequestDispatcher(DASH_URN).forward(request, response);
    }

}
