package com.excilys.cdb.persistence.dao.mysql;

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

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.EntityField;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;
import com.excilys.cdb.persistence.dao.ICompanyDao.CompanyField;
import com.excilys.cdb.persistence.dao.IComputerDao;


/**
 * MySQL implementation of IComputerDao.
 *
 * @author Nicolas THIERION.
 * @version 0.2.0
 */
@Repository("computerDao")
public class ComputerDao implements IComputerDao {

    /* ***
     * DB REQUESTS
     * ***/

    /** various sql script user to build preparedStatements. */
    private static final String REQ_SELECT_COMPUTERS_FILENAME = "select_computers.hql";
    private static final String REQ_COUNT_COMPUTERS_FILENAME  = "select_computer_count.hql";
    private static final String REQ_DELETE_COMPUTER_FILENAME  = "delete_computer_with_id.hql";

    /* ***
     * ATTRIBUTES
     */
    /** SQL query strings. */
    private Map<String, String> mQueryStrings;

    /** provides a session to submit HQL queries to datasource. */
    @Autowired
    private SessionFactory      mSessionFactory;
    /* ***
     * CONSTRUCTORS / DESTRUCTORS
     */
    /**
     * loads SQL query strings from resources.
     */
    private ComputerDao() {
        mLoadHqlQueries();
    }

    /**
     * @deprecated this is no more a singleton.
     * @return unique instance of DAO.
     */
    @Deprecated
    public static ComputerDao getInstance() {
        return new ComputerDao();
    }

    /**
     * Constructor with argument. Create a new ComputerDao that will use the
     * given SessionFactory to submit queries to the DB.
     *
     * @param sessionFactory
     *            SessionFactory to access the DB.
     */
    public ComputerDao(SessionFactory sessionFactory) {
        mLoadHqlQueries();
        mSessionFactory = sessionFactory;
    }

    /* ***
     * ACCESSORS
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        mSessionFactory = sessionFactory;
    }

    /* ***
     * DAO SERVICES
     */

    @Override
    public List<Computer> listEqual(EntityField<Computer> field, String value, int offset, int count)
            throws IllegalArgumentException {
        // check offset parameter
        if (offset < 0) {
            throw new IllegalArgumentException("search offset cannot be negative");
        }

        // check queried parameter
        if (value == null) {
            throw new IllegalArgumentException("search parameter cannot be null");
        }

        // check field validity
        mCheckField(field);

        // check count validity
        count = (count <= 0 ? Integer.MAX_VALUE : count);

        // get HQL query string & format it
        String hqlStr = mQueryStrings.get(REQ_SELECT_COMPUTERS_FILENAME);
        // place field criteria by hand...
        hqlStr = String.format(hqlStr, field.getLabel(), field.getLabel());
        final Query query = mGetCurrentSession().createQuery(hqlStr);

        query.setString("value", value);

        // set range parameters
        query.setFirstResult(offset).setMaxResults(count);

        // exec query
        @SuppressWarnings("unchecked")
        final List<Computer> resList = query.list();

        return resList;
    }

    @Override
    public int getCount() {
        return getCountLike(ComputerField.ID, "");
    }

    @Override
    public int getCountEqual(EntityField<Computer> field, String value) throws IllegalArgumentException, DaoException {

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }

        // check field validity
        mCheckField(field);

        // get HQL query string & format it
        String hqlStr = mQueryStrings.get(REQ_COUNT_COMPUTERS_FILENAME);
        // place field criteria & order by hand...
        hqlStr = String.format(hqlStr, field.getLabel());
        final Query query = mGetCurrentSession().createQuery(hqlStr);
        query.setString("value", value);

        // execute the query
        final Long count = (Long) query.uniqueResult();
        return count.intValue();
    }

    @Override
    public Computer add(Computer computer) throws DaoException {
        // ensure that we are attempting to add a NEW computer (with id
        // field = null)"
        final Long id = computer.getId();
        if (id != null) {
            throw new IllegalArgumentException("Trying to add a computer : " + computer
                    + " with non-blank field \"computer id\"");
        }

        // save computer
        try {
            mGetCurrentSession().save(computer);
        } catch (final Exception e) {

        }
        // ensure computer has been saved
        if (computer.getId() == null || computer.getId() == 0) {
            throw new DaoException(new StringBuilder().append("Something went wrong when adding computer ")
                    .append(computer).append(". No changes commited.").toString(), ErrorType.SQL_ERROR);
        }


        return computer;
    }

    /**
     * Update the given computer from DB.
     *
     */
    @Override
    public Computer update(Computer computer) {
        if (computer.getId() == null) {
            throw new IllegalArgumentException("Cannot update computer with id = null");
        }

        if (getCountEqual(ComputerField.ID, "" + computer.getId()) == 0 ){
            throw new NoSuchElementException("No computer with id = " + computer.getId());
        }

        // save computer
        try {
            mGetCurrentSession().update(computer);
        } catch (final Exception e) {
            throw new DaoException(new StringBuilder().append("Something went wrong when updating computer ")
                    .append(computer).append(". No changes commited.").toString(), ErrorType.SQL_ERROR);
        }

        return computer;
    }

    /**
     * Delete the computer with given id from DB.
     *
     * @throws DaoException
     *             if deletion failed or if provided computer is invalid or
     *             doesn't exist.
     */
    @Override
    public void delete(Long id) throws DaoException, IllegalArgumentException {

        // Delete computer by id : ensure computer has id != null
        if (id == null) {
            throw new IllegalArgumentException("Computer id is null. Cannot delete this computer");
        }

        final String hqlStr = mQueryStrings.get(REQ_DELETE_COMPUTER_FILENAME);
        final Query query = mGetCurrentSession().createQuery(hqlStr);
        query.setParameter("id", id);

        if (query.executeUpdate() != 1) {
            throw new NoSuchElementException("Something went wrong while deleting computer no " + id
                    + ". Maybe this computer doesn't exist?");
        }
    }

    /* ***
     * PRIVATE METHODS
     */

    /**
     * init prepared statement used for various DAO services.
     */
    private void mLoadHqlQueries() {

        mQueryStrings = new HashMap<String, String>();

        try {
            QueryUtils.loadHqlQuery(REQ_SELECT_COMPUTERS_FILENAME, mQueryStrings);
            QueryUtils.loadHqlQuery(REQ_COUNT_COMPUTERS_FILENAME, mQueryStrings);
            QueryUtils.loadHqlQuery(REQ_DELETE_COMPUTER_FILENAME, mQueryStrings);

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
        }
    }

    private void mCheckField(EntityField<Computer> field) {
        if (!(field instanceof ComputerField)) {
            throw new IllegalArgumentException("Field must be of type " + CompanyField.class.getName());
        }
    }

    private Session mGetCurrentSession() {
        if (mSessionFactory == null || mSessionFactory.isClosed()) {
            throw new InstantiationError("No session has been given to this dao. Abording...");
        }
        return mSessionFactory.getCurrentSession();
    }
}
