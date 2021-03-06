package com.excilys.cdb.cli;

import java.util.List;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.mysql.CompanyDao;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ICrudService;

public class CliContext {

    /* ***
     * ATTRIBUTES
     */
    private final ICrudService<Long, Computer> mComputerService = new ComputerService(ComputerDao.getInstance());
    private final ICrudService<Long, Company>  mCompanyService  = new CompanyService(CompanyDao.getInstance(),
                                                                  ComputerDao.getInstance());

    private List<Computer>         mComputers;
    private List<Company>          mCompanies;
    private Computer               mNewComputer;
    private long                   mComputerId;
    private final Scanner          mScanner;
    private boolean                mExit            = false;

    public CliContext(Scanner scanner) {
        mScanner = scanner;
    }

    /* ***
     * ACCESSORS
     */
    public List<Computer> getComputers() {
        return mComputers;
    }

    public List<Company> getCompanies() {
        return mCompanies;
    }

    public long getComputerId() {
        return mComputerId;
    }

    public Computer getNewComputer() {
        return mNewComputer;
    }

    public void setComputers(List<Computer> computers) {
        mComputers = computers;
    }

    public void setCompanies(List<Company> companies) {
        mCompanies = companies;
    }

    public void setComputerId(long computerId) {
        mComputerId = computerId;
    }

    public void setNewComputer(Computer newComputer) {
        mNewComputer = newComputer;
    }

    public ICrudService<Long, Computer> getComputerService() {
        return mComputerService;
    }

    public ICrudService<Long, Company> getCompanyService() {
        return mCompanyService;
    }

    public Scanner getScanner() {
        return mScanner;
    }

    public void setExit(boolean exit) {
        mExit = exit;
    }

    public boolean isExit() {
        return mExit;
    }

}
