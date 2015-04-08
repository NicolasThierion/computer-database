package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.dao.mysql.CompanyDao;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * Servlet implementation to handle 'add computer' page.
 */
@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {

    private static final long   serialVersionUID = 8756374436015233990L;

    /* ***
     * CONSTANTS
     */
    /** jsp to redirect to. */
    private static final String JSP_URI          = "/WEB-INF/views/addComputer.jsp";

    /** input parameters. sent by JSP. */
    private static class ReqParam {
        /** if should update computer. */
        private static final String IS_UPDATE        = "update";
        /** name of computer to update. */
        private static final String COMPUTER_NAME    = "computerName";
        /** release date of computer to update. */
        private static final String COMPUTER_RELEASE = "introduced";
        /** discontinuation date of computer to update. */
        private static final String COMPUTER_DISCONT = "discontinued";
        /** company id of computer to update. */
        private static final String COMPANY_ID       = "companyId";
    }

    /** output parameters. */
    private static class ResParam {
        /** List of companies to be sent to JSP. */
        private static final String COMPANIES_PAGE_BEAN = "companiesPageBean";
    }

    /* ***
     * ATTRIBUTES
     */
    private IComputerService mComputerService;
    private ICompanyService  mComanyService;

    private final Long       mComputerId   = null;
    private boolean          mShouldUpdate = false;
    private String           mComputerName;
    private String           mComputerRelease;
    private String           mComputerDiscont;
    private Long             mCompanyId    = null;


    @Override
    public void init() {
        mComputerService = new ComputerService(ComputerDao.getInstance());
        mComanyService = new CompanyService(CompanyDao.getInstance(), ComputerDao.getInstance());
    }

    @Override
    public void destroy() {
        mComputerService = null;
        mComanyService = null;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
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
        mCheckParameters(request, response);

        if (mShouldUpdate) {
            mDoAdd(request, response);
        }
        mGotoAdd(request, response);
    }

    /* ***
     * PRIVATE METHODS
     */
    private void mDoAdd(HttpServletRequest request, HttpServletResponse response) {
        final Computer computer = new ComputerDto(mComputerId, mComputerName, mComputerRelease, mComputerDiscont,
                mCompanyId).toComputer();
        mComputerService.add(computer);
    }

    private void mGotoAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // fetch company list for <select>
        final List<Company> companies = mComanyService.listByName();
        final Page<Company> companiesPage = new Page<Company>(companies);

        // set attributes & redirect to jsp.
        request.setAttribute(ResParam.COMPANIES_PAGE_BEAN, companiesPage);
        getServletContext().getRequestDispatcher(JSP_URI).forward(request, response);
    }

    /**
     * fetch requested fields or parameters for editComputer JSP.
     *
     * @param request
     * @param response
     */
    private void mCheckParameters(HttpServletRequest request, HttpServletResponse response) {

        final String isUpdateStr = request.getParameter(ReqParam.IS_UPDATE);
        mShouldUpdate = (isUpdateStr != null ? new Boolean(isUpdateStr) : false);

        // if we came here back from editComputer => update flag is true.
        if (mShouldUpdate) {
            mComputerName = request.getParameter(ReqParam.COMPUTER_NAME);
            mComputerRelease = request.getParameter(ReqParam.COMPUTER_RELEASE);
            mComputerDiscont = request.getParameter(ReqParam.COMPUTER_DISCONT);
            final String companyIdStr = request.getParameter(ReqParam.COMPANY_ID);
            mCompanyId = (companyIdStr != null ? Long.parseLong(companyIdStr) : null);
            mCompanyId = (mCompanyId == 0 ? null : mCompanyId);
        }
    }
}
