package com.excilys.cdb.service;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ConnectionFactory;
import com.excilys.cdb.persistence.Transaction;
import com.excilys.cdb.persistence.dao.ICompanyDao;
import com.excilys.cdb.persistence.dao.IComputerDao;
import com.excilys.cdb.persistence.mapper.CompanyMapper;
import com.excilys.cdb.persistence.mapper.ComputerMapper;

/**
 * Spring-autowired Company service. Offers CRUD services for companies.
 *
 * @author Nicolas THIERION.
 *
 */
@Service
public class CompanyService implements ICompanyService {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    /* ***
     * ATTRIBUTES
     */
    /** Computer & Company DAO used by this Service. */
    @Autowired
    private ICompanyDao             mCompanyDao;
    @Autowired
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

    /**
     * Default constructor. Will not be usable until ComputerService &
     * CompanyService has been set.
     *
     */
    public CompanyService() {
    }

    /* ***
     * ACCESSORS
     */
    /**
     * Set the {@code #ICompanyDao} to be used by this Service.
     *
     * @param companyDao
     *            CompanyDao to use.
     */
    public void setCompanyDao(ICompanyDao companyDao) {
        mCompanyDao = companyDao;
    }

    /**
     * Set the {@code #IComputerDao} to be used by this Service.
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
    public List<Company> listLikeName(int offset, int count, String name) {
        return mCompanyDao.listLike(CompanyMapper.Field.NAME, name, offset, count);
    }

    @Override
    public List<Company> listByName(int begin, int nb) {
        LOG.info("listByName(" + begin + ", " + nb + ")");
        return mCompanyDao.listBy(CompanyMapper.Field.NAME, begin, nb);
    }

    @Override
    public int getCount() {
        return mCompanyDao.getCount();
    }

    @Override
    public int getCount(String name) {
        LOG.info("getCount(" + name + ")");
        return mCompanyDao.getCountEqual(CompanyMapper.Field.NAME, name);
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
        LOG.info("search(" + companyId + ")");
        if (companyId <= 0) {
            throw new IllegalArgumentException("Company id must be positive");
        }

        final List<Company> list = mCompanyDao.listLike(CompanyMapper.Field.ID, "" + companyId);
        return (list.size() == 0 ? null : list.get(0));
    }

    /**
     * delete company with given ID a delete computers that belongs to this
     * company.
     *
     * @param id
     *            Id of company to delete.
     * @throws NoSuchElementException
     *             if no valid computerDao has been found.
     */
    @Transactional
    @Override
    public void delete(Long id) throws NoSuchElementException {
        LOG.info("delete(" + id + ")");
        mAssertComputerDao();

        final Transaction transaction = mConnectionFactory.getTransaction();
        try {
            try {
                transaction.begin();
                final List<Computer> computers = mComputerDao.listEqual(ComputerMapper.Field.COMPANY_ID,
                        Long.toString(id));
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
            throw new NoSuchElementException(e.getMessage());
        }
        mConnectionFactory.close(transaction);
    }


    @Override
    public Company add(Company company) throws IllegalArgumentException {
        LOG.info("add(" + company + ")");
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
