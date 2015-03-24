package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@WebServlet("/editComputer")
public class EditDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 8756374436015233990L;

    /* ***
     * CONSTANTS
     */
    /** uri to redirect to when accessing to '/'. */
    private static final String              DASH_URI            = "/WEB-INF/views/dashboard.jsp";
    private static final String              SEARCH_COMPUTER_URI = "/searchComputer";
    private static final String              EDIT_COMPUTER_URI   = "/editComputer";
    private static final String              ROOT_REDIRECT_URI   = DASH_URI;

    private static final String              HEADER_URI          = "/header.jsp";

    private static final Map<String, String> JSP_DISPAT          = new HashMap<String, String>();
    {
        JSP_DISPAT.put(SEARCH_COMPUTER_URI, "/WEB-INF/views/dashboard.jsp");
        JSP_DISPAT.put(EDIT_COMPUTER_URI, "/WEB-INF/views/editComputer.jsp");
    }


    private static final int    PAGE_LENGTH      = 10;

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
    public EditDashboardServlet() {
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
        String action = request.getPathInfo();

        if (action == null) {
            action = ROOT_REDIRECT_URI;
        }

        if (action.equals(HEADER_URI)) {
            mIncludeHeader(request, response);
        } else if (action.equals(DASH_URI)) {
            // handle dashboard
            mGotoDashboard(request, response);
            return;
        } else if (action.equals(SEARCH_COMPUTER_URI)) {
            // handle dashboard with search
            mGotoDashboard(request, response);
        } else if (action.equals(EDIT_COMPUTER_URI)) {
            mGotoEditComputer(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /* ***
     * PRIVATE METHODS
     */

    private void mIncludeHeader(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    IOException {
        getServletContext().getRequestDispatcher(JSP_DISPAT.get(HEADER_URI)).forward(request, response);

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
        getServletContext().getRequestDispatcher(JSP_DISPAT.get(DASH_URI)).forward(request, response);
    }

    private void mGotoEditComputer(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    IOException {

        // set result page & send redirect.
        getServletContext().getRequestDispatcher(JSP_DISPAT.get(EDIT_COMPUTER_URI)).forward(request, response);

    }

}
