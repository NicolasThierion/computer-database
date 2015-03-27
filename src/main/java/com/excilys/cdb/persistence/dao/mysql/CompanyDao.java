package com.excilys.cdb.persistence.dao.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.ConnectionFactory;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.ICompanyDao;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;
import com.excilys.cdb.persistence.mapper.CompanyMapper;

/**
 * MySQL immplementation of ICompanyDao.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 */
public final class CompanyDao implements ICompanyDao {

    /* ***
     * DB REQUESTS
     */
    /** various sql script user to build preparedStatements. */
    private static final String REQ_SELECT_COMPANIES_FILENAME = "select_companies_paging.sql";
    private static final String REQ_SELECT_COMPANY_FILENAME   = "select_company.sql";
    private static final String REQ_COUNT_COMPANIES_FILENAME  = "select_company_count.sql";

    /* ***
     * ATTRIBUTES
     */
    /** Singleton's instance. */
    private static CompanyDao   mInstance                     = null;
    private Map<String, String> mQueryStrings;

    /* ***
     * CONSTRUCTORS / DESTRUCTORS
     */
    private CompanyDao() {
        mLoadSqlQueries();
    }

    /**
     *
     * @return unique instance of DAO.
     */
    public static CompanyDao getInstance() {
        synchronized (CompanyDao.class) {
            if (mInstance == null) {
                mInstance = new CompanyDao();
            }
        }
        return mInstance;
    }

    /* ***
     * DAO SERVICES
     */

    @Override
    public List<Company> listByName(int begin, int nb) throws DaoException {

        final String name = "%%";
        return listByName(begin, nb, name);
    }

    @Override
    public List<Company> listByName(int begin, int nb, String name) {

        Connection dbConn = null;
        PreparedStatement selectCompaniesStatement = null;
        ResultSet result = null;
        final List<Company> resList = new LinkedList<Company>();

        // check parameters
        begin = (begin < 0 ? 0 : begin);
        nb = (nb < 0 ? Integer.MAX_VALUE : nb);
        name = name.toUpperCase();

        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            selectCompaniesStatement = dbConn.prepareStatement(mQueryStrings.get(REQ_SELECT_COMPANIES_FILENAME));

            // set range parameters
            selectCompaniesStatement.setString(1, name);
            selectCompaniesStatement.setInt(2, begin);
            selectCompaniesStatement.setInt(3, nb);

            // exec query
            result = selectCompaniesStatement.executeQuery();
            // parse resultSet to build the list of computers.
            while (result.next()) {

                // get company name;
                final int compId = result.getInt("id");
                final String compName = result.getString("name");
                final Company company = new Company(compId, compName);
                resList.add(company);
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, selectCompaniesStatement, result);
        }
        return resList;
    }

    @Override
    public Company searchById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("company ID must be positive.");
        }

        Connection dbConn = null;
        PreparedStatement selectCompanyStatement = null;
        ResultSet result = null;
        Company company = null;


        final String sqlStr = mQueryStrings.get(REQ_SELECT_COMPANY_FILENAME);
        try {

            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            selectCompanyStatement = dbConn.prepareStatement(sqlStr);

            // set range parameters
            selectCompanyStatement.setLong(1, id);

            // exec query
            result = selectCompanyStatement.executeQuery();
            if (result.first()) {
                final CompanyMapper mapper = new CompanyMapper();
                company = mapper.fromResultSet(result);
            }

        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, selectCompanyStatement, result);
        }
        return company;
    }

    @Override
    public int getCount() {
        return getCount("");
    }

    @Override
    public int getCount(String name) throws IllegalArgumentException {
        Connection dbConn = null;
        PreparedStatement countCompaniesStatement = null;
        ResultSet result = null;

        // check name parameter
        if (name == null) {
            throw new IllegalArgumentException("Company name cannot be null");
        }
        name = "%" + name + "%";
        int count = 0;
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            countCompaniesStatement = dbConn.prepareStatement(mQueryStrings
                    .get(REQ_COUNT_COMPANIES_FILENAME));
            countCompaniesStatement.setString(1, name);
            result = countCompaniesStatement.executeQuery();
            while (result.next()) {
                count = result.getInt(1);
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, countCompaniesStatement, result);
        }
        return count;
    }

    /* ***
     * PRIVATE METHODS
     */

    private void mLoadSqlQueries() {
        mQueryStrings = new HashMap<String, String>();

        try {
            SqlUtils.loadSqlQuery(REQ_SELECT_COMPANIES_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_SELECT_COMPANY_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_COUNT_COMPANIES_FILENAME, mQueryStrings);


        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

}
