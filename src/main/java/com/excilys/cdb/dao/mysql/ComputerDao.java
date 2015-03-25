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
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerMapper;


/**
 * MySQL immplementation of IComputerDao.
 *
 * @author Nicolas THIERION
 * @version 0.2.0
 */
public final class ComputerDao implements IComputerDao {

    /* ***
     * DB REQUESTS
     * ***/

    /** various sql script user to build preparedStatements. */
    private static final String REQ_SELECT_COMPUTERS_FILENAME = "select_computers_paging.sql";
    private static final String REQ_SELECT_COMPUTER_FILENAME  = "select_computer.sql";
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
    public List<Computer> listByName(int begin, int nb) throws DaoException, IllegalArgumentException {
        final String name = "";
        return listLikeName(begin, nb, name);
    }

    @Override
    public List<Computer> listLikeName(int offset, int nb, String name) throws IllegalArgumentException {
        Connection dbConn = null;
        PreparedStatement selectComputersStatement = null;
        ResultSet result = null;
        final List<Computer> resList = new LinkedList<Computer>();
        final String sqlStr = mQueryStrings.get(REQ_SELECT_COMPUTERS_FILENAME);

        // check offset parameter
        if (offset < 0) {
            throw new IllegalArgumentException("search offset cannot be negative");
        }

        // check name parameter
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        nb = (nb < 0 ? Integer.MAX_VALUE : nb);
        name = "%".concat(name.toUpperCase()).concat("%");

        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            selectComputersStatement = dbConn.prepareStatement(sqlStr);

            //set range parameters
            int colId = 1;
            selectComputersStatement.setString(colId++, name);
            selectComputersStatement.setInt(colId++, offset);
            selectComputersStatement.setInt(colId++, nb);

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
    public Computer searchById(long id) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException("computer ID must be positive.");
        }

        Connection dbConn = null;
        PreparedStatement selectComputersStatement = null;
        ResultSet result = null;
        final String sqlStr = mQueryStrings.get(REQ_SELECT_COMPUTER_FILENAME);

        Computer computer = null;
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            selectComputersStatement = dbConn.prepareStatement(sqlStr);

            // set range parameters
            selectComputersStatement.setLong(1, id);

            // exec query
            result = selectComputersStatement.executeQuery();
            if (result.first()) {
                final ComputerMapper mapper = new ComputerMapper();
                computer = mapper.fromResultSet(result);
            }

        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, selectComputersStatement, result);
        }
        return computer;
    }

    @Override
    public int getCount() {
        return getCount("");
    }

    @Override
    public int getCount(String name) throws IllegalArgumentException, DaoException {

        Connection dbConn = null;
        PreparedStatement countComputersStatement = null;
        ResultSet result = null;
        int count = 0;
        final String sqlStr = mQueryStrings.get(REQ_COUNT_COMPUTERS_FILENAME);

        // check name parameter
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            countComputersStatement = dbConn.prepareStatement(sqlStr);

            name = "%".concat(name.toUpperCase()).concat("%");
            countComputersStatement.setString(1, name);
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
    public void add(Computer computer) throws DaoException {
        Connection dbConn = null;
        PreparedStatement insertComputerStatement = null;
        ResultSet result = null;
        final String sqlStr = mQueryStrings.get(REQ_INSERT_COMPUTER_FILENAME);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            insertComputerStatement = dbConn.prepareStatement(sqlStr);

            //ensure that we are attempting to add a NEW computer (with id field = null)"
            final Long id = computer.getId();
            if (id != null) {
                throw new DaoException("Trying to add a computer : " + computer
                        + " with non-blank field \"computer id\"",
                        ErrorType.SQL_ERROR);
            }

            //build SQL request :
            // INSERT INTO computer (name, introduced, discontinued, company_id)
            //get computer properties

            final ComputerMapper h = new ComputerMapper();
            h.fromEntity(computer);

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
                throw new DaoException("Something went wrong while deleting compuer " + computer
                        + ". Maybe this computer doesn't exist?",
                        ErrorType.SQL_ERROR);
            }
        } catch (final SQLException e) {
            throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
        } finally {
            SqlUtils.safeCloseAll(dbConn, updateComputerStatement, null);
        }
        return computer;
    }

    /**
     * Delete the given computer from DB.
     * @throws DaoException if deletion failed or if provided computer is invalid or doesn't exist.
     */
    @Override
    public void delete(Computer computer) throws DaoException, IllegalArgumentException {
        Connection dbConn = null;
        PreparedStatement deleteComputerStatement = null;
        //Delete computer by id : ensure computer has id != null
        if (computer.getId() == null) {
            throw new IllegalArgumentException("Computer id is null. Cannot delete this computer");
        }
        final String sqlStr = mQueryStrings.get(REQ_DELETE_COMPUTER_FILEMANE);
        try {
            // get a connection & prepare needed statement
            dbConn = ConnectionFactory.getInstance().getConnection();
            deleteComputerStatement = dbConn.prepareStatement(sqlStr);

            deleteComputerStatement.setLong(1, computer.getId());
            if (deleteComputerStatement.executeUpdate() != 1) {
                throw new DaoException("Something went wrong while deleting compuer " + computer
                        + ". Maybe this computer doesn't exist?",
                        ErrorType.SQL_ERROR);
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
            SqlUtils.loadSqlQuery(REQ_SELECT_COMPUTER_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_COUNT_COMPUTERS_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_INSERT_COMPUTER_FILENAME, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_UPDATE_COMPUTER_FILEMANE, mQueryStrings);
            SqlUtils.loadSqlQuery(REQ_DELETE_COMPUTER_FILEMANE, mQueryStrings);

        } catch (final IOException e) {
            throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
        }
    }

}
