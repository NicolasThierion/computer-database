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
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.ConnectionFactory;
import com.excilys.cdb.persistence.EntityField;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;
import com.excilys.cdb.persistence.dao.ICompanyDao;
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
    private static final String REQ_COUNT_COMPANIES_FILENAME  = "select_company_count.sql";
    private static final String REQ_INSERT_COMPANY_FILENAME   = "insert_company.sql";
    private static final String REQ_DELETE_COMPANY_FILENAME   = "delete_company_with_id.sql";

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
    public List<Company> listEqual(EntityField<Company> field, String value, int offset, int count) {

        Connection dbConn = null;
        PreparedStatement selectCompaniesStatement = null;
        ResultSet result = null;
        final List<Company> resList = new LinkedList<Company>();

        // check offset parameter
        if (offset < 0) {
            throw new IllegalArgumentException("search offset cannot be negative");
        }

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        count = (count < 0 ? Integer.MAX_VALUE : count);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            String sqlStr = mQueryStrings.get(REQ_SELECT_COMPANIES_FILENAME);
            sqlStr = String.format(sqlStr, field.getLabel(), field.getLabel());
            selectCompaniesStatement = dbConn.prepareStatement(sqlStr);

            // set range parameters
            int colId = 1;
            selectCompaniesStatement.setString(colId++, value);
            selectCompaniesStatement.setInt(colId++, offset);
            selectCompaniesStatement.setInt(colId++, count);

            // exec query
            result = selectCompaniesStatement.executeQuery();
            // parse resultSet to build the list of computers.
            while (result.next()) {

                // get company name;
                final CompanyMapper companyMap = new CompanyMapper();
                final Company company = companyMap.fromResultSet(result);
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
    public int getCount() {
        return getCountLike(CompanyMapper.Field.ID, "");
    }

    @Override
    public int getCountEqual(EntityField<Company> field, String value) throws IllegalArgumentException {
        Connection dbConn = null;
        PreparedStatement countCompaniesStatement = null;
        ResultSet result = null;

        String sqlStr = mQueryStrings.get(REQ_COUNT_COMPANIES_FILENAME);
        sqlStr = String.format(sqlStr, field.getLabel());

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("Company name cannot be null");
        }
        int count = 0;
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            countCompaniesStatement = dbConn.prepareStatement(sqlStr);
            countCompaniesStatement.setString(1, value);
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

    @Override
    public Company add(Company company) {
        Connection dbConn = null;
        PreparedStatement insertCompanyStatement = null;
        ResultSet result = null;
        final String sqlStr = mQueryStrings.get(REQ_INSERT_COMPANY_FILENAME);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            insertCompanyStatement = dbConn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);

            // ensure that we are attempting to add a NEW computer (with id
            // field = null)"
            final Long id = company.getId();
            if (id != null) {
                throw new IllegalArgumentException("Trying to add a company : " + company
                        + " with non-blank field \"company id\"");
            }

            // build SQL request :
            // INSERT INTO computer (name, introduced, discontinued, company_id)
            // get computer properties

            final CompanyMapper h = new CompanyMapper();
            h.fromEntity(company);

            int colId = 1;
            insertCompanyStatement.setString(colId++, h.getName());

            if (insertCompanyStatement.executeUpdate() != 1) {
                throw new DaoException(
                        "Something went wrong when adding company " + company
                        + ". No changes commited.", ErrorType.SQL_ERROR);
            }

            // get generated id...
            result = insertCompanyStatement.getGeneratedKeys();
            if (result.next()) {
                // & update this computer with new generated id.
                company.setId(result.getLong(1));
            }
            result.close();
            return company;
        } catch (final SQLException e) {
            throw new DaoException("Something went wrong when adding company " + company + " : " + e.getMessage(),
                    ErrorType.UNKNOWN_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, insertCompanyStatement, result);
        }
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
        Connection dbConn = null;
        PreparedStatement deleteCompanyStatement = null;
        // Delete computer by id : ensure computer has id != null
        if (id == null) {
            throw new IllegalArgumentException("Company id is null. Cannot delete this company");
        }
        final String sqlStr = mQueryStrings.get(REQ_DELETE_COMPANY_FILENAME);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            deleteCompanyStatement = dbConn.prepareStatement(sqlStr);

            deleteCompanyStatement.setLong(1, id);
            if (deleteCompanyStatement.executeUpdate() != 1) {
                throw new NoSuchElementException("Something went wrong while deleting company no " + id
                        + ". Maybe this company doesn't exist?");
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, deleteCompanyStatement, null);
        }
    }

    /* ***
     * PRIVATE METHODS
     */

    private void mLoadSqlQueries() {
        mQueryStrings = new HashMap<String, String>();

        try {
            SqlUtils.loadSqlQuery(REQ_SELECT_COMPANIES_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_COUNT_COMPANIES_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_INSERT_COMPANY_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_DELETE_COMPANY_FILENAME, mQueryStrings);

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        }
    }
}
