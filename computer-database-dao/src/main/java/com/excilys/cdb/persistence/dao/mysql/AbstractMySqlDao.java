package com.excilys.cdb.persistence.dao.mysql;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;

@Transactional
public class AbstractMySqlDao<T> {

    /* ***
     * ATTRIBUTES
     */
    private Map<String, String> mQueryStrings;

    /** provides a session to submit HQL queries to datasource. */
    @Autowired
    private SessionFactory      mSessionFactory;

    /* ***
     * CONSTRUCTOR
     */
    public AbstractMySqlDao(String... hqlFilenames) {
        mLoadHqlQueries(hqlFilenames);
    }

    /* ***
     * PUBLIC METHODS
     */

    protected Session getCurrentSession() {
        if (mSessionFactory == null || mSessionFactory.isClosed()) {
            throw new InstantiationError("No session has been given to this dao. Abording...");
        }
        return mSessionFactory.getCurrentSession();
    }

    protected String getQuery(String filename) {
        return mQueryStrings.get(filename);
    }

    protected void daoSave(T model) {
        mSessionFactory.getCurrentSession().save(model);
    }

    protected void daoUpdate(T model) {
        mSessionFactory.getCurrentSession().update(model);
    }

    protected void daoDelete(T model) {
        mSessionFactory.getCurrentSession().delete(model);
    }

    /* ***
     * ACCESSORS
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        mSessionFactory = sessionFactory;
    }

    public Query createQuery(String hql) {
        return getCurrentSession().createQuery(hql);
    }

    /* ***
     * PRIVATE METHODS
     */

    private void mLoadHqlQueries(String... hqlFilenames) {
        mQueryStrings = new HashMap<String, String>();
        new File(".").getAbsolutePath();
        try {

            for (final String filename : hqlFilenames) {
                QueryUtils.loadHqlQuery(filename, mQueryStrings);
            }

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }



}
