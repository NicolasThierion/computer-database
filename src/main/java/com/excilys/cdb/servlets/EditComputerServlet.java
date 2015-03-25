package com.excilys.cdb.servlets;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dao.mysql.CompanyDao;
import com.excilys.cdb.dao.mysql.ComputerDao;
import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * Servlet implementation to handle 'edit computer' page.
 */
@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet {

    private static final long serialVersionUID = 8756374436015233990L;

    /* ***
     * CONSTANTS
     */
    /** jsp to redirect to. */
    private static final String EDIT_COMPUTER_URI = "/WEB-INF/views/editComputer.jsp";

    /** input parameters. */
    private static class ReqParam {
        /** Id of computer to edit. */
        private static final String COMPUTER_ID = "computerId";

    }

    /** output parameters. */
    private static class ResParam {
        /** Computer attribute to be sent to JSP. */
        private static final String COMPUTER_BEAN = "computerBean";
        /** List of companies to be sent to JSP. */
        private static final String COMPANIES_PAGE_BEAN = "companiesPageBean";

    }

    /* ***
     * ATTRIBUTES
     */
    private IComputerService mComputerService;
    private ICompanyService  mComanyService;

    private int              mComputerId = -1;

    @Override
    public void init() {
        mComputerService = new ComputerService(ComputerDao.getInstance());
        mComanyService = new CompanyService(CompanyDao.getInstance());
    }

    @Override
    public void destroy() {
        mComputerService = null;
        mComanyService = null;
    }


    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerServlet() {
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
        mGotoEdit(request, response);
    }

    /* ***
     * PRIVATE METHODS
     */
    private void mGotoEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        mCheckParameters(request, response);

        // create a new computer from computerId.
        final ComputerDto computer = ComputerDto.fromComputer(mComputerService.retrieve(mComputerId));

        // fetch company list for <select>
        final List<Company> companies = mComanyService.listByName();
        final Page<Company> companiesPage = new Page<Company>(companies);

        // set attributes & redirect to jsp.
        request.setAttribute(ResParam.COMPUTER_BEAN, computer);
        request.setAttribute(ResParam.COMPANIES_PAGE_BEAN, companiesPage);
        getServletContext().getRequestDispatcher(EDIT_COMPUTER_URI).forward(request, response);
    }

    /**
     * fetch computer id parameter.
     *
     * @param request
     * @param response
     */
    private void mCheckParameters(HttpServletRequest request, HttpServletResponse response) {
        // ensure computer id is present
        final String computerIdStr = request.getParameter(ReqParam.COMPUTER_ID);

        if (computerIdStr != null && !computerIdStr.trim().isEmpty()) {
            mComputerId = Integer.parseInt(computerIdStr);
            return;
        }
        throw new InvalidParameterException("missing computerId parameter. Cannot edit.");

    }
}
