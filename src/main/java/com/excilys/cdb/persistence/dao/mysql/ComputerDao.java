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
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ConnectionFactory;
import com.excilys.cdb.persistence.EntityField;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.DaoException.ErrorType;
import com.excilys.cdb.persistence.dao.IComputerDao;
import com.excilys.cdb.persistence.mapper.ComputerMapper;


/**
 * MySQL implementation of IComputerDao.
 *
 * @author Nicolas THIERION.
 * @version 0.2.0
 */
public final class ComputerDao implements IComputerDao {

    /* ***
     * DB REQUESTS
     * ***/

    /** various sql script user to build preparedStatements. */
    private static final String REQ_SELECT_COMPUTERS_FILENAME = "select_computers_paging.sql";
    private static final String REQ_COUNT_COMPUTERS_FILENAME = "select_computer_count.sql";
    private static final String REQ_UPDATE_COMPUTER_FILEMANE = "update_computer.sql";
    private static final String REQ_INSERT_COMPUTER_FILENAME = "insert_computer.sql";
    private static final String REQ_DELETE_COMPUTER_FILEMANE = "delete_computer_with_id.sql";

    /* ***
     * ATTRIBUTES
     */
    /** Singleton's instance. */
    private static ComputerDao mInstance = null;
    /** SQL query strings. */
    private Map<String, String> mQueryStrings;

    /* ***
     * CONSTRUCTORS / DESTRUCTORS
     */
    /**
     * loads SQL query strings from resources.
     */
    private ComputerDao() {
        mLoadSqlQueries();
    }

    /**
     * @return unique instance of DAO.
     */
    public static ComputerDao getInstance() {
        synchronized (ComputerDao.class) {
            if (mInstance == null) {
                mInstance = new ComputerDao();
            }
        }
        return mInstance;
    }

    /* ***
     * DAO SERVICES
     */

    @Override
    public List<Computer> listEqual(EntityField<Computer> field, String value, int offset, int count)
            throws IllegalArgumentException {
        Connection dbConn = null;
        PreparedStatement selectComputersStatement = null;
        ResultSet result = null;
        final List<Computer> resList = new LinkedList<Computer>();
        String sqlStr = mQueryStrings.get(REQ_SELECT_COMPUTERS_FILENAME);
        sqlStr = String.format(sqlStr, field.getLabel(), field.getLabel());

        // check offset parameter
        if (offset < 0) {
            throw new IllegalArgumentException("search offset cannot be negative");
        }

        // check queried parameter
        if (value == null) {
            throw new IllegalArgumentException("search parameter cannot be null");
        }

        count = (count < 0 ? Integer.MAX_VALUE : count);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            selectComputersStatement = dbConn.prepareStatement(sqlStr);

            // set "search by" parameter
            int colId = 1;

            // set "range" parameter
            selectComputersStatement.setString(colId++, value);
            selectComputersStatement.setInt(colId++, offset);
            selectComputersStatement.setInt(colId++, count);

            //exec query
            result = selectComputersStatement.executeQuery();

            final HashMap<Long, Company> companiesMap = new HashMap<Long, Company>();

            //parse resultSet to build the list of computers.
            while (result.next()) {

                final ComputerMapper h = new ComputerMapper();
                h.fromResultSet(result);

                Company company;

                //store used companies in a map in order to not construct same company twice.
                if (!companiesMap.containsKey(new Long(h.getCompanyId()))) {
                    company = new Company(h.getCompanyId(), h.getCompanyName());
                    companiesMap.put(new Long(h.getCompanyId()), company);
                } else {
                    company = companiesMap.get(new Long(h.getCompanyId()));
                }

                //finally, create the computer & add it to the list
                final Computer computer = new Computer(h.getId(), h.getName());
                computer.setCompany(company);
                computer.setReleaseDate(h.getReleaseDate());
                computer.setDiscontDate(h.getDiscDate());
                resList.add(computer);
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, selectComputersStatement, result);
        }
        return resList;
    }

    @Override
    public int getCount() {
        return getCountLike(ComputerMapper.Field.ID, "");
    }

    @Override
    public int getCountEqual(EntityField<Computer> field, String value) throws IllegalArgumentException, DaoException {

        Connection dbConn = null;
        PreparedStatement countComputersStatement = null;
        ResultSet result = null;
        int count = 0;
        String sqlStr = mQueryStrings.get(REQ_COUNT_COMPUTERS_FILENAME);
        sqlStr = String.format(sqlStr, field.getLabel());

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }

        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            countComputersStatement = dbConn.prepareStatement(sqlStr);

            countComputersStatement.setString(1, value);
            result = countComputersStatement.executeQuery();
            if (result.first()) {
                count = result.getInt(1);
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, countComputersStatement, result);
        }
        return count;
    }

    @Override
    public Computer add(Computer computer) throws DaoException {
        Connection dbConn = null;
        PreparedStatement insertComputerStatement = null;
        ResultSet result = null;
        final String sqlStr = mQueryStrings.get(REQ_INSERT_COMPUTER_FILENAME);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            insertComputerStatement = dbConn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);

            // ensure that we are attempting to add a NEW computer (with id
            // field = null)"
            final Long id = computer.getId();
            if (id != null) {
                throw new IllegalArgumentException("Trying to add a computer : " + computer
                        + " with non-blank field \"computer id\"");
            }

            // build SQL request :
            // INSERT INTO computer (name, introduced, discontinued, company_id)
            // get computer properties

            final ComputerMapper h = new ComputerMapper();
            h.fromEntity(computer);

            // ensure company id is valid
            if (h.getCompany() != null && h.getCompanyId() < 1) {
                throw new IllegalArgumentException("Company id must be positive.");
            }
            int colId = 1;
            insertComputerStatement.setString(colId++, h.getName());
            insertComputerStatement.setTimestamp(colId++, h.getSqlReleaseDate());
            insertComputerStatement.setTimestamp(colId++, h.getSqlDiscDate());

            if (h.getCompanyId() == null) {
                insertComputerStatement.setNull(colId++, java.sql.Types.INTEGER);
            } else {
                insertComputerStatement.setLong(colId++, h.getCompanyId());
            }
            if (insertComputerStatement.executeUpdate() != 1) {
                throw new DaoException("Something went wrong when adding computer " + computer
                        + ". No changes commited.", ErrorType.SQL_ERROR);
            }

            //get generated id...
            result = insertComputerStatement.getGeneratedKeys();
            if (result.next()) {
                //& update this computer with new generated id.
                computer.setId(result.getLong(1));
            }
            result.close();
            return computer;
        } catch (final SQLException e) {
            throw new DaoException("Something went wrong when adding computer " + computer + " : " + e.getMessage(),
                    ErrorType.UNKNOWN_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, insertComputerStatement, result);
        }
    }

    /**
     * Update the given computer from DB.
     *
     * @throws DaoException
     *             if update failed or if provided computer is invalid or
     *             doesn't exist.
     */
    @Override
    public Computer update(Computer computer) throws DaoException, IllegalArgumentException {
        Connection dbConn = null;
        PreparedStatement updateComputerStatement = null;
        // Update computer by id : ensure computer has id != null
        if (computer.getId() == null) {
            throw new IllegalArgumentException("Computer id is null. Cannot update this computer");
        }
        final String sqlStr = mQueryStrings.get(REQ_UPDATE_COMPUTER_FILEMANE);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            updateComputerStatement = dbConn.prepareStatement(sqlStr);

            //retrieve computer information
            final ComputerMapper h = new ComputerMapper();
            h.fromEntity(computer);

            //build query
            //UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;

            int colId = 1;
            updateComputerStatement.setString(colId++, h.getName());
            updateComputerStatement.setTimestamp(colId++, h.getSqlReleaseDate());
            updateComputerStatement.setTimestamp(colId++, h.getSqlDiscDate());

            if (h.getCompanyId() == null) {
                updateComputerStatement.setNull(colId++, java.sql.Types.INTEGER);
            } else {
                updateComputerStatement.setLong(colId++, h.getCompanyId());
            }
            updateComputerStatement.setLong(colId++, h.getId());

            if (updateComputerStatement.executeUpdate() != 1) {
                throw new NoSuchElementException("Something went wrong while updating computer " + computer
                        + ". Maybe this computer doesn't exist?");
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, updateComputerStatement, null);
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
        Connection dbConn = null;
        PreparedStatement deleteComputerStatement = null;
        //Delete computer by id : ensure computer has id != null
        if (id == null) {
            throw new IllegalArgumentException("Computer id is null. Cannot delete this computer");
        }
        final String sqlStr = mQueryStrings.get(REQ_DELETE_COMPUTER_FILEMANE);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            deleteComputerStatement = dbConn.prepareStatement(sqlStr);

            deleteComputerStatement.setLong(1, id);
            if (deleteComputerStatement.executeUpdate() != 1) {
                throw new NoSuchElementException("Something went wrong while deleting computer no " + id
                        + ". Maybe this computer doesn't exist?");
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, deleteComputerStatement, null);
        }
    }

    /* ***
     * PRIVATE METHODS
     */

    /**
     * init prepared statement used for various DAO services.
     */
    private void mLoadSqlQueries() {

        mQueryStrings = new HashMap<String, String>();

        try {
            SqlUtils.loadSqlQuery(REQ_SELECT_COMPUTERS_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_COUNT_COMPUTERS_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_INSERT_COMPUTER_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_UPDATE_COMPUTER_FILEMANE, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_DELETE_COMPUTER_FILEMANE, mQueryStrings);

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
        }
    }
}
