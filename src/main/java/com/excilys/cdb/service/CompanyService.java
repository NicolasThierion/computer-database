package com.excilys.cdb.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.model.Company;

public class CompanyService implements ICompanyService {

    /* ***
     * ATTRIBUTES
     */
    /** Company DAO used by this Service. */
    private ICompanyDao mCompanyDao;

    /* ***
     * CONSTRUCTORS
     */
    /**
     * Argument constructor. Creates a new Service, given company DAO
     * implementation.
     *
     * @param companyDao
     *            Company DAO to be used by this service
     */
    public CompanyService(ICompanyDao companyDao) {
        mCompanyDao = companyDao;
    }

    /* ***
     * ACCESSORS
     */
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
        return mCompanyDao != null;
    }

    /* ***
     * SERVICE METHODS
     */

    @Override
    public List<Company> listByName() {
        return listByName(0, Integer.MAX_VALUE);
    }

    @Override
    public List<Company> listByName(int begin, int nb) {
        return mCompanyDao.listByName(begin, nb);
    }

    @Override
    public int getCount() {
        return mCompanyDao.getCount();
    }

    @Override
    public int getCount(String name) {
        return mCompanyDao.getCount(name);
    }

    @Override
    public Company retrieve(long companyId) {
        final Company company = search(companyId);
        if (company == null) {
            throw new NoSuchElementException("no company with id = " + companyId + " can be found");
        }
        return company;
    }

    @Override
    public Company search(long companyId) {
        return mCompanyDao.searchById(companyId);
    }


}
