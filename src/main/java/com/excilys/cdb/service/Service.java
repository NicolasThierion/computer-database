package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


/**
 *
 * @author Nicolas THIERION.
 * @version 0.1.0
 */
public class Service implements IService {

    /* ***
     * ATTRIBUTES
     */
    /** Computer DAO used by this Service. */
    private IComputerDao mComputerDao;
    /** Company DAO used by this Service. */
    private ICompanyDao  mCompanyDao;

    /* ***
     * CONSTRUCTORS
     */
    /**
     * Argument constructor. Creates a nes Service, given the computer DAO &
     * company DAO implementations.
     *
     * @param computerDao
     *            Computer DAO to be used by this service.
     * @param companyDao
     *            Company DAO to be used by this service
     */
    public Service(IComputerDao computerDao, ICompanyDao companyDao) {
        mComputerDao = computerDao;
        mCompanyDao = companyDao;
    }

    /**
     * Default constructor. Create a new Service with no DAO. You should call
     * {@link #setCompanyDao(ICompanyDao)} &
     * {@link #setComputerDao(IComputerDao)} on this instance to be able to use
     * the service properly.
     */
    public Service() {
        mCompanyDao = null;
        mComputerDao = null;
    }

    /* ***
     * ACCESSORS
     */
    /**
     * Sets the {@code IComputerDao} to be used by this Service.
     *
     * @param computerDao
     *            Computer Dao to use.
     */
    public void setComputerDao(IComputerDao computerDao) {
        mComputerDao = computerDao;
    }

    /**
     * Sets the {@code ICompanyDao} to be used by this Service.
     *
     * @param companyDao
     *            CompanyDao to use.
     */
    public void setCompanyDao(ICompanyDao companyDao) {
        mCompanyDao = companyDao;
    }

    /**
     * Tells if this instance is properly initialized (ie : Correct DAO has been
     * given).
     *
     * @return true if initialized.
     */
    public boolean isInitialized() {
        return mCompanyDao != null && mComputerDao != null;
    }

    /* ***
     * SERVICE METHODS
     */


    @Override
    public List<Computer> listComputersByName(int offset, int count) {
        mCheckInit();
        return mComputerDao.listByName(offset, count);
    }

    @Override
    public List<Computer> listComputersLikeName(int offset, int count, String name) {
        return mComputerDao.listLikeName(offset, count, name);
    }

    @Override
    public List<Company> listCompaniesByName(int begin, int nb) {
        mCheckInit();
        return mCompanyDao.listByName(begin, nb);
    }

    @Override
    public void addComputer(Computer computer) {
        mComputerDao.add(computer);
    }

    @Override
    public void updateComputer(Computer computer) {
        mComputerDao.update(computer);
    }

    @Override
    public void deleteComputer(Computer computer) {
        mComputerDao.delete(computer);
    }

    /* ***
     * PRIVATE METHODS
     */

    private void mCheckInit() throws ServiceException {
        if (isInitialized()) {
            return;
        }

        String error = "Service not initialized.";
        if (mComputerDao == null) {
            error = error.concat("Computer DAO not initialized");
        } else if (mCompanyDao == null) {
            error = error.concat("Company DAO not initialized");
        }

        throw new ServiceException(error);
    }

    @Override
    public int getComputerCount() {
        return mComputerDao.getCount();
    }



}
