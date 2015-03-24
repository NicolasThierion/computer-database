package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.LinkedList;
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

    private static final int    DEFAULT_PAGE_SIZE      = 10;
    private static final String DASH_URN         = "/WEB-INF/views/dashboard.jsp";

    private static final String SEARCH_VARNAME   = "search";
    private static final String PAGE_SIZE_VARNAME = "pageSize";
    private static final String PAGE_OFFSET_VARNAME = "offset";

    /* ***
     * ATTRIBUTES
     */
    private ICompanyDao         mCompanyDao;
    private IComputerDao        mComputerDao;
    private IService            mService;
    private int                 mPageSize           = DEFAULT_PAGE_SIZE;
    private int                 mOffset             = 0;
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

        List<Computer> computers = new LinkedList<Computer>();

        // presence of search parameter?
        String queryName = request.getParameter(SEARCH_VARNAME);
        // search empty name if name is null => search for all computers.
        if (queryName == null) {
            queryName = "";
        }

        // presence of pageSize parameter?
        final String pageSizeStr = request.getParameter(PAGE_SIZE_VARNAME);
        if (pageSizeStr != null && !pageSizeStr.trim().isEmpty()) {
            mPageSize = Integer.parseInt(pageSizeStr.trim());
        }

        // presence of offset parameter?
        final String pageOffsetStr = request.getParameter(PAGE_OFFSET_VARNAME);
        if (pageOffsetStr != null && !pageOffsetStr.trim().isEmpty()) {
            mOffset = Integer.parseInt(pageOffsetStr.trim());
        }

        // count results & store them in a Page<Computer>
        final int totalResults = mService.getComputersCount(queryName);

        if (totalResults > 0) {
            // select all computer in page range, & ensure offset is not too far.
            computers = mService.listComputersLikeName(mOffset, mPageSize, queryName);
            if (mOffset > totalResults) {
                mOffset = totalResults;
            }

        }


        // TODO varying offset
        final Page<Computer> page = new Page<Computer>(computers, mOffset, totalResults, queryName);

        // set result page & send redirect.
        request.setAttribute("resultsPageBean", page);
        getServletContext().getRequestDispatcher(DASH_URN).forward(request, response);
    }

}
