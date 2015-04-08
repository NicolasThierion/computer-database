package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.IComputerService;

/**
 * Servlet implementation to handle dashboard page.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 8756374436015233990L;

    /* ***
     * CONSTANTS
     */
    /** Default amount of result to display int he page. */
    private static final int    DEFAULT_PAGE_SIZE = 10;
    /** jsp to redirect to. */
    private static final String JSP_URI          = "/WEB-INF/views/dashboard.jsp";

    /** input parameters. */
    private static class ReqParam {
        /** search parameter name. */
        private static final String SEARCH      = Page.Field.SEARCH.getLabel();
        /** page size parameter name. */
        private static final String PAGE_SIZE   = Page.Field.SIZE.getLabel();
        /** search offset parameter name. */
        private static final String PAGE_OFFSET = Page.Field.OFFSET.getLabel();
    }

    /** output parameters. */
    private static class ResParam {
        /** Page attribute to be sent to JSP. */
        private static final String PAGE_BEAN = "pageBean";
    }

    /** parameters of this context. */
    private class ServletContext {
        public int    pageSize = DEFAULT_PAGE_SIZE;
        public int    offset   = 0;
        public String queryName;
    }

    /* ***
     * ATTRIBUTES
     */
    private IComputerService mComputerService;

    @Override
    public void init() {
        mComputerService = new ComputerService(ComputerDao.getInstance());
    }

    @Override
    public void destroy() {
        mComputerService = null;
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
        mDoGetOrPost(request, response);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        mDoGetOrPost(request, response);
    }

    private void mDoGetOrPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        ServletUtils.logRequest(request, response);
        mGotoDashboard(request, response);
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

        // get page variables
        final ServletContext context = mCheckParameters(request, response);

        // count results & store them in a Page<Computer>
        final int totalResults = mComputerService.getCount(context.queryName);

        if (totalResults > 0) {
            // select all computer in page range, & ensure offset is not too
            // far.
            computers = mComputerService.listLikeName(context.offset, context.pageSize, context.queryName);
            if (context.offset > totalResults) {
                context.offset = totalResults;
            }
        }

        final List<ComputerDto> dtos = ComputerDto.fromComputers(computers);
        final Page<ComputerDto> page = new Page<ComputerDto>(dtos, context.offset, totalResults, context.queryName);
        page.setSize(context.pageSize);

        // set result page & send redirect.
        request.setAttribute(ResParam.PAGE_BEAN, page);
        getServletContext().getRequestDispatcher(JSP_URI).forward(request, response);
    }

    private ServletContext mCheckParameters(HttpServletRequest request, HttpServletResponse response) {

        final ServletContext context = new ServletContext();

        // presence of search parameter?
        context.queryName = request.getParameter(ReqParam.SEARCH);
        // search empty name if name is null => search for all computers.
        if (context.queryName == null) {
            context.queryName = "";
        }

        // presence of pageSize parameter?
        final String pageSizeStr = request.getParameter(ReqParam.PAGE_SIZE);
        if (pageSizeStr != null && !pageSizeStr.trim().isEmpty()) {
            context.pageSize = Integer.parseInt(pageSizeStr.trim());
        }

        // presence of offset parameter?
        final String offsetStr = request.getParameter(ReqParam.PAGE_OFFSET);
        if (offsetStr != null && !offsetStr.trim().isEmpty()) {
            context.offset = Integer.parseInt(offsetStr.trim());
        }
        return context;
    }
}
