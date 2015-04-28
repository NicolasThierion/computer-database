package com.excilys.cdb.persistence.dao.mysql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.EntityField;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;
import com.excilys.cdb.persistence.dao.ICompanyDao;

/**
 * MySQL immplementation of ICompanyDao.
 *
 * @author Nicolas THIERION
 * @version 0.3.0
 */
@Repository("companyDao")
public class CompanyDao implements ICompanyDao {

    /* ***
     * DB REQUESTS
     */
    /** various sql script user to build preparedStatements. */
    private static final String REQ_SELECT_COMPANIES_FILENAME = "select_companies.hql";
    private static final String REQ_COUNT_COMPANIES_FILENAME  = "select_company_count.hql";
    private static final String REQ_DELETE_COMPANY_FILENAME   = "delete_company_with_id.hql";

    /* ***
     * ATTRIBUTES
     */
    /** Singleton's instance. */
    private Map<String, String> mQueryStrings;

    /** provides a session to submit HQL queries to datasource. */
    @Autowired
    private SessionFactory      mSessionFactory;


    /* ***
     * CONSTRUCTORS / DESTRUCTORS
     */

    /**
     * Default constructor. Create an empty CompanyDao. This Dao is not usable
     * until a valid SessionFactoryt has been set. A valid SessionFactory is a
     * SessionFactory that has access to DB. It will be used to submit query to
     * DB.
     */
    public CompanyDao() {
        mLoadHqlQueries();
    }

    /**
     * Constructor with argument. Create a new CompanyDao that will use the
     * given SessionFactory to submit queries to the DB.
     *
     * @param sessionFactory
     *            SessionFactory to access the DB.
     */
    public CompanyDao(SessionFactory sessionFactory) {
        mLoadHqlQueries();
        mSessionFactory = sessionFactory;
    }

    /* ***
     * ACCESSORS
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        mSessionFactory = sessionFactory;
    }


    /**
     * @deprecated this is no more a singleton.
     * @return unique instance of DAO.
     */
    @Deprecated
    public static CompanyDao getInstance() {
        return new CompanyDao();
    }

    /* ***
     * DAO SERVICES
     */
    @Override
    public List<Company> listEqual(EntityField<Company> field, String value, int offset, int count) {


        // check offset parameter
        if (offset < 0) {
            throw new IllegalArgumentException("search offset cannot be negative");
        }

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        // check field validity
        mCheckField(field);

        count = (count < 0 ? Integer.MAX_VALUE : count);

        // get a connection & prepare needed statement

        // get HQL query string & format it
        String hqlStr = mQueryStrings.get(REQ_SELECT_COMPANIES_FILENAME);
        // place field criteria & order by hand...
        hqlStr = String.format(hqlStr, field.getLabel(), field.getLabel());
        final Query query = mGetCurrentSession().createQuery(hqlStr);

        query.setString("value", value);

        // set range parameters
        query.setFirstResult(offset).setMaxResults(count);

        // execute the query
        @SuppressWarnings("unchecked")
        final List<Company> resList = query.list();
        return resList;
    }

    @Override
    public int getCount() {
        return getCountLike(CompanyField.ID, "");
    }

    @Override
    public int getCountEqual(EntityField<Company> field, String value) throws IllegalArgumentException {
        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("Company name cannot be null");
        }
        // check field validity
        mCheckField(field);

        // get HQL query string & format it
        String hqlStr = mQueryStrings.get(REQ_COUNT_COMPANIES_FILENAME);
        // place field criteria by hand...
        hqlStr = String.format(hqlStr, field.getLabel());
        final Query query = mGetCurrentSession().createQuery(hqlStr);
        query.setString("value", value);

        // execute the query
        final Long count = (Long) query.uniqueResult();
        return count.intValue();
    }

    @Override
    public Company add(Company company) {

        // ensure that we are attempting to add a NEW computer (with id
        // field = null)"
        final Long id = company.getId();
        if (id != null) {
            throw new IllegalArgumentException("Trying to add a company : " + company
                    + " with non-blank field \"company id\"");
        }

        // save company
        mGetCurrentSession().save(company);
        // ensure company has been saved.
        if (company.getId() == null || company.getId() == 0) {
            throw new DaoException(new StringBuilder().append("Something went wrong when adding company ")
                    .append(company).append(". No changes commited.").toString(), ErrorType.SQL_ERROR);
        }

        return company;
    }

    /**
     * Delete the company with given id from DB.
     *
     * @throws DaoException
     *             if deletion failed or if provided computer is invalid or
     *             doesn't exist.
     */
    @Override
    public void delete(Long id) throws DaoException, IllegalArgumentException {

        // Delete company by id : ensure computer has id != null
        if (id == null) {
            throw new IllegalArgumentException("Company id is null. Cannot delete this company");
        }

        final String hqlStr = mQueryStrings.get(REQ_DELETE_COMPANY_FILENAME);
        final Query query = mGetCurrentSession().createQuery(hqlStr);
        query.setParameter("id", id);

        if (query.executeUpdate() != 1) {
            throw new NoSuchElementException("Something went wrong while deleting company no " + id
                    + ". Maybe this company doesn't exist?");
        }
    }

    /* ***
     * PRIVATE METHODS
     */
    private Session mGetCurrentSession() {
        if (mSessionFactory == null || mSessionFactory.isClosed()) {
            throw new InstantiationError("No session has been given to this dao. Abording...");
        }
        return mSessionFactory.getCurrentSession();
    }

    private void mLoadHqlQueries() {
        mQueryStrings = new HashMap<String, String>();
        new File(".").getAbsolutePath();
        try {
            QueryUtils.loadHqlQuery(REQ_SELECT_COMPANIES_FILENAME, mQueryStrings);
            QueryUtils.loadHqlQuery(REQ_COUNT_COMPANIES_FILENAME, mQueryStrings);
            QueryUtils.loadHqlQuery(REQ_DELETE_COMPANY_FILENAME, mQueryStrings);

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

    private void mCheckField(EntityField<Company> field) {
        if (!(field instanceof CompanyField)) {
            throw new IllegalArgumentException("Field must be of type " + CompanyField.class.getName());
        }
    }
}
