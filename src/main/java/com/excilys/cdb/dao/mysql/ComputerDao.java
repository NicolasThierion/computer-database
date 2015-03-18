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
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.dao.DaoException.ErrorType;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerMapper;


/**
 * 
 * 
 * @author Nicolas THIERION
 * @version 0.2.0 
 */
public class ComputerDao implements IComputerDao {

	/* ***
	 * DB REQUESTS
	 * ***/

	/** various sql script user to build preparedStatements */
	private static final String REQ_SELECT_COMPUTERS_FILENAME = "select_computers_paging.sql";
	private static final String REQ_COUNT_COMPUTERS_FILENAME = "select_computer_count.sql";
	private static final String REQ_UPDATE_COMPUTER_FILEMANE = "update_computer.sql";
	private static final String REQ_INSERT_COMPUTER_FILENAME = "insert_computer.sql";
	private static final String REQ_DELETE_COMPUTER_FILEMANE = "delete_computer_with_id.sql";

	/* ***
	 * ATTRIBUTES
	 */
	/** Singleton's instance */
	private static ComputerDao mInstance = null;
	/** SQL query strings */
	private Map<String, String> mQueryStrings;

	/* ***
	 * CONSTRUCTORS / DESTRUCTORS
	 */
	private ComputerDao() {
		mLoadSqlQueries();
	}

	/**
	 * 
	 * @return unique instance of DAO.
	 */
	public static ComputerDao getInstance() {
		synchronized(ComputerDao.class) {
			if(mInstance == null) {
				mInstance = new ComputerDao();	
			}
		}
		return mInstance;
	}

	/* ***
	 * DAO SERVICES
	 */
	@Override
	public List<Computer> listByName(int begin, int nb) throws DaoException {

		String name = "%%";
		return listByName(begin, nb, name);
	}


	@Override
	public List<Computer> listByName(int begin, int nb, String name) {

		try (
				//get a connection & prepare needed statement
				Connection dbConn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement selectComputersStatement = dbConn.prepareStatement(mQueryStrings.get(REQ_SELECT_COMPUTERS_FILENAME));
				) {
			begin = (begin<0 ? 0 : begin);
			nb = (nb<0 ? Integer.MAX_VALUE : nb);

			name = name.toUpperCase();
			//set range parameters
			selectComputersStatement.setString(1, name);
			selectComputersStatement.setInt(2, begin);
			selectComputersStatement.setInt(3, nb);

			List<Computer> resList = new LinkedList<Computer>();

			//exec query
			ResultSet res = selectComputersStatement.executeQuery();

			HashMap<Long, Company> companiesMap = new HashMap<Long, Company>();

			//parse resultSet to build the list of computers.
			while(res.next()) {

				ComputerMapper h = new ComputerMapper();
				h.fromResultSet(res);

				Company company;

				//store used companies in a map in order to not construct same company twice.
				if(!companiesMap.containsKey(new Long(h.companyId))) {
					company = new Company(h.companyId, h.companyName);
					companiesMap.put(new Long(h.companyId), company);
				}
				else {
					company = companiesMap.get(new Long(h.companyId));
				}

				//finally, create the computer & add it to the list
				Computer computer = new Computer(h.id, h.name);
				computer.setCompany(company);
				computer.setReleaseDate(h.releaseDate);
				computer.setDiscontDate(h.discDate);
				resList.add(computer);
			}
			return resList;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
	}

	@Override
	public int getCount() {
		int count = 0;
		try (
				//get a connection & prepare needed statement
				Connection dbConn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement countComputersStatement = dbConn.prepareStatement(mQueryStrings.get(REQ_COUNT_COMPUTERS_FILENAME));
				) {
			ResultSet res = countComputersStatement.executeQuery();
			if(res.first()) {
				count = res.getInt(1);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
		return count;
	}


	/**
	 * Calling this method will assign a new "computer id" to the computer.
	 * @throw DaoException when trying to add an invalid computer. An invalid computer is a computer with a non blank field "Computer id".
	 * See "updateComputer()" if you want to update computer information of an existing computer.
	 */
	@Override
	public void add(Computer computer) throws DaoException {
		try (
				//get a connection & prepare needed statement
				Connection dbConn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement insertComputerStatement = dbConn.prepareStatement(mQueryStrings.get(REQ_INSERT_COMPUTER_FILENAME));
				) {
			//ensure that we are attempting to add a NEW computer (with id field = null)"
			Long id = computer.getId();
			if(id != null ) {
				throw new DaoException("Trying to add a computer : " + computer + " with non-blank field \"computer id\"", ErrorType.SQL_ERROR);
			}

			//build SQL request : 
			// INSERT INTO computer (name, introduced, discontinued, company_id)
			//get computer properties

			ComputerMapper h = new ComputerMapper();
			h.fromComputer(computer);

			insertComputerStatement.setString(1, h.name);
			insertComputerStatement.setTimestamp(2, h.sqlReleaseDate);
			insertComputerStatement.setTimestamp(3, h.sqlDiscDate);

			if(h.companyId == null) {
				insertComputerStatement.setNull(4, java.sql.Types.INTEGER);
			}
			else {
				insertComputerStatement.setLong(4, h.companyId);
			}		 
			if( insertComputerStatement.executeUpdate() != 1) {
				throw new DaoException("Something went wrong when adding computer " + computer + ". No changes commited.", ErrorType.SQL_ERROR);
			}

			//get generated id...
			ResultSet rs = insertComputerStatement.getGeneratedKeys();
			if (rs.next()){
				//& update this computer with new generated id.
				computer.setId(rs.getLong(1));
			} 
		} catch (SQLException e) {
			throw new DaoException("Something went wrong when adding computer " + computer + " : " + e.getMessage(), ErrorType.UNKNOWN_ERROR);
		}
	}

	/**
	 * Update the given computer from DB.
	 * @throws DaoException if deletion failed or if provided computer is invalid or doesn't exist.
	 */
	@Override
	public Computer update(Computer computer) {
		//Delete computer by id : ensure computer has id != null
		if(computer.getId() == null)
			throw new DaoException("Computer id is null. Cannot update this computer", ErrorType.DAO_ERROR);

		try (
				//get a connection & prepare needed statement
				Connection dbConn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement updateComputerStatement = dbConn.prepareStatement(mQueryStrings.get(REQ_UPDATE_COMPUTER_FILEMANE));
				) {
			//retrieve computer information
			ComputerMapper h = new ComputerMapper();
			h.fromComputer(computer);

			//build query
			//UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;

			updateComputerStatement.setString(1, h.name);
			updateComputerStatement.setTimestamp(2, h.sqlReleaseDate);
			updateComputerStatement.setTimestamp(3, h.sqlDiscDate);

			if(h.companyId == null) {
				updateComputerStatement.setNull(4, java.sql.Types.INTEGER);
			}
			else {
				updateComputerStatement.setLong(4, h.companyId);
			}
			updateComputerStatement.setLong(5, h.id);

			if(updateComputerStatement.executeUpdate() != 1) {
				throw new DaoException("Something went wrong while deleting compuer " + computer + ". Maybe this computer doesn't exist?", 
						ErrorType.SQL_ERROR);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
		return computer;
	}

	/**
	 * Delete the given computer from DB.
	 * @throws DaoException if deletion failed or if provided computer is invalid or doesn't exist.
	 */
	@Override
	public void delete(Computer computer) throws DaoException{
		//Delete computer by id : ensure computer has id != null
		if(computer.getId() == null)
			throw new DaoException("Computer id is null. Cannot delete this computer", ErrorType.DAO_ERROR);

		try (
				//get a connection & prepare needed statement
				Connection dbConn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement deleteComputerStatement = dbConn.prepareStatement(mQueryStrings.get(REQ_DELETE_COMPUTER_FILEMANE));
				) {
			deleteComputerStatement.setLong(1, computer.getId());
			if(deleteComputerStatement.executeUpdate() != 1) {
				throw new DaoException("Something went wrong while deleting compuer " + computer + ". Maybe this computer doesn't exist?", 
						ErrorType.SQL_ERROR);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
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

		} catch (IOException e) {
			throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
		}
	}
}
