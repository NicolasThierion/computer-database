package com.excilys.cdb.service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ConnectionFactory;
import com.excilys.cdb.persistence.Transaction;
import com.excilys.cdb.persistence.dao.ICompanyDao;
import com.excilys.cdb.persistence.dao.IComputerDao;
import com.excilys.cdb.persistence.mapper.CompanyMapper;
import com.excilys.cdb.persistence.mapper.ComputerMapper;

public class CompanyService implements ICompanyService {

    /* ***
     * ATTRIBUTES
     */
    /** Computer & Company DAO used by this Service. */
    private ICompanyDao             mCompanyDao;
    private IComputerDao            mComputerDao;

    private final ConnectionFactory mConnectionFactory = ConnectionFactory.getInstance();


    /* ***
     * CONSTRUCTORS
     */
    /**
     * Argument constructor. Creates a new Service, given company DAO
     * implementation.
     *
     * @param companyDao
     *            Company DAO to be used by this service
     * @param computerDao
     *            Computer DAO to be used by this service
     */
    public CompanyService(ICompanyDao companyDao, IComputerDao computerDao) {
        mCheckParams(companyDao, computerDao);
        mCompanyDao = companyDao;
        mComputerDao = computerDao;
    }


    /**
     * Create a new CompanyService without providing COmputerDao. Some functions
     * will not be available.
     *
     * @param companyDao
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
     * Sets the {@code IComputerDao} to be used by this Service.
     *
     * @param computerDao
     *            ComputerDao to use.
     */
    public void setCompanyDao(IComputerDao computerDao) {
        mComputerDao = computerDao;
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
        return mCompanyDao.listBy(CompanyMapper.Field.NAME, begin, nb);
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
        return mCompanyDao.searchBy(CompanyMapper.Field.ID, "" + companyId);
    }

    /**
     * delete company with given ID a delete computers that belongs to this
     * company.
     *
     * @param id
     *            Id of company to delete.
     * @throws ServiceException
     *             if no valid computerDao has been found.
     */
    @Override
    public void delete(Long id) throws NoSuchElementException, ServiceException {
        mAssertComputerDao();

        final Transaction transaction = mConnectionFactory.getTransaction();
        try {
            try {
                transaction.begin();
                final List<Computer> computers = mComputerDao.listBy(ComputerMapper.Field.NAME);
                // delete all computers that matches this company...
                for (final Computer computer : computers) {
                    mComputerDao.delete(computer);
                }
                // ... & delete this company
                mCompanyDao.delete(id);
                transaction.commit();
                transaction.end();
            } catch (final SQLException e) {
                transaction.rollback();
                throw new ServiceException(e.getMessage());
            } finally {
                transaction.end();
            }
        } catch (final SQLException e) {
            throw new ServiceException(e.getMessage());
        }
        mConnectionFactory.close(transaction);
    }

    @Override
    public List<Company> listLikeName(int offset, int count, String name) {
        return mCompanyDao.listLike(CompanyMapper.Field.NAME, name, offset, count);
    }

    @Override
    public Company add(Company company) throws IllegalArgumentException {
        return mCompanyDao.add(company);
    }

    /* ***
     * PRIVATE FUNCTIONS
     */
    private void mAssertComputerDao() throws ServiceException {
        if (mComputerDao == null) {
            throw new ServiceException("This service use no ComputerDao, but it is required for this function.");
        }
    }

    private void mCheckParams(ICompanyDao companyDao, IComputerDao computerDao) {
        if (companyDao == null) {
            throw new NullPointerException("CompanyDao cannot be null");
        }

        if (computerDao == null) {
            throw new NullPointerException("ComputerDao cannot be null");
        }
    }


}
