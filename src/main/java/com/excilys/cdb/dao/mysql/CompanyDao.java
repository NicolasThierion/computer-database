package com.excilys.cdb.dao.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.excilys.cdb.dao.ConnectionFactory;
import com.excilys.cdb.dao.DaoException;
import com.excilys.cdb.dao.DaoException.ErrorType;
import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.model.Company;

/**
 *
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

        try (
        // get a connection & prepare needed statement
        Connection dbConn = ConnectionFactory.getInstance().getConnection();
                PreparedStatement selectCompaniesStatement = dbConn.prepareStatement(mQueryStrings
                        .get(REQ_SELECT_COMPANIES_FILENAME));) {
            begin = (begin < 0 ? 0 : begin);
            nb = (nb < 0 ? Integer.MAX_VALUE : nb);

            name = name.toUpperCase();
            // set range parameters
            selectCompaniesStatement.setString(1, name);
            selectCompaniesStatement.setInt(2, begin);
            selectCompaniesStatement.setInt(3, nb);

            final List<Company> resList = new LinkedList<Company>();

            // exec query
            final ResultSet res = selectCompaniesStatement.executeQuery();

            // parse resultSet to build the list of computers.
            while (res.next()) {

                // get company name;
                final int compId = res.getInt("id");
                final String compName = res.getString("name");
                final Company company = new Company(compId, compName);
                resList.add(company);
            }

            return resList;


        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }

    @Override
    public int getCount() {

        int count = 0;
        try (
        // get a connection & prepare needed statement
        Connection dbConn = ConnectionFactory.getInstance().getConnection();
                PreparedStatement countCompaniesStatement = dbConn.prepareStatement(mQueryStrings
                        .get(REQ_COUNT_COMPANIES_FILENAME));) {
            final ResultSet res = countCompaniesStatement.executeQuery();
            while (res.next()) {
                count = res.getInt(1);
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
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
            SqlUtils.loadSqlQuery(REQ_COUNT_COMPANIES_FILENAME, mQueryStrings);

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }
}
