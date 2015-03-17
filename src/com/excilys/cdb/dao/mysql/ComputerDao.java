package com.excilys.cdb.dao.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
	/** where SQL scripts are stored */
	private static final File SQL_DIR = new File("res/sql");

	/** various sql script user to build preparedStatements */
	private static final String REQ_SELECT_COMPUTERS_FILENAME = "select_computers_paging.sql";
	private static final String REQ_COUNT_COMPUTERS_FILENAME = "select_computer_count.sql";
	private static final String REQ_UPDATE_COMPUTER = "update_computer.sql";
	private static final String REQ_INSERT_COMPUTER = "insert_computer.sql";
	private static final String REQ_DELETE_COMPUTER = "delete_computer_with_id.sql";

	/* ***
	 * PREPARED STATEMENTS
	 */
	private List<PreparedStatement> mStatements;
	private PreparedStatement mSelectComputersStatement;
	private PreparedStatement mCountComputersStatement;
	private PreparedStatement mInsertComputerStatement;
	private PreparedStatement mUpdateComputerStatement;
	private PreparedStatement mDeleteComputerStatement;

	/* ***
	 * ATTRIBUTES
	 */
	/** Singleton's instance */
	private static ComputerDao mInstance = null;

	/** unique DB connection */
	private Connection mDbConn = ConnectionFactory.getInstance().getConnection();

	/* ***
	 * CONSTRUCTORS / DESTRUCTORS
	 */
	private ComputerDao() {
		mInitStatements();
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

	
	/**
	 * free up memory & close resources
	 */
	public void destroy() {
		try {
			mDbConn.close();
		} catch (SQLException e1) {
			throw new DaoException(e1.getMessage(), ErrorType.SQL_ERROR);
		}
		
		for(PreparedStatement statement : mStatements) {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
				}
			}
		}	
	}

	@Override
	public void finalize() {
		destroy();
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

		try {
			begin = (begin<0 ? 0 : begin);
			nb = (nb<0 ? Integer.MAX_VALUE : nb);

			name = name.toUpperCase();
			//set range parameters
			mSelectComputersStatement.setString(1, name);
			mSelectComputersStatement.setInt(2, begin);
			mSelectComputersStatement.setInt(3, nb);

		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}

		List<Computer> resList = new LinkedList<Computer>();

		try {
			//exec query
			ResultSet res = mSelectComputersStatement.executeQuery();

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

		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
		return resList;
	
	}


	@Override
	public int getCount() {
		int count = 0;
		try {
			ResultSet res = mCountComputersStatement.executeQuery();
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
		try {
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

			mInsertComputerStatement.setString(1, h.name);
			mInsertComputerStatement.setTimestamp(2, h.sqlReleaseDate);
			mInsertComputerStatement.setTimestamp(3, h.sqlDiscDate);

			if(h.companyId == null) {
				mInsertComputerStatement.setNull(4, java.sql.Types.INTEGER);
			}
			else {
				mInsertComputerStatement.setLong(4, h.companyId);
			}		 
			if( mInsertComputerStatement.executeUpdate() != 1) {
				throw new DaoException("Something went wrong when adding computer " + computer + ". No changes commited.", ErrorType.SQL_ERROR);
			}

			//get generated id...
			ResultSet rs = mInsertComputerStatement.getGeneratedKeys();
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

		try {
			//retrieve computer information
			ComputerMapper h = new ComputerMapper();
			h.fromComputer(computer);

			//build query
			//UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;

			mUpdateComputerStatement.setString(1, h.name);
			mUpdateComputerStatement.setTimestamp(2, h.sqlReleaseDate);
			mUpdateComputerStatement.setTimestamp(3, h.sqlDiscDate);
			
			if(h.companyId == null) {
				mUpdateComputerStatement.setNull(4, java.sql.Types.INTEGER);
			}
			else {
				mUpdateComputerStatement.setLong(4, h.companyId);
			}
			mUpdateComputerStatement.setLong(5, h.id);
			
			if(mUpdateComputerStatement.executeUpdate() != 1) {
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

		try {
			mDeleteComputerStatement.setLong(1, computer.getId());
			int res = mDeleteComputerStatement.executeUpdate();

			//ensure everything is OK
			if(res != 1) {
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
	private void mInitStatements() {

		mStatements = new LinkedList<PreparedStatement>();
		try {
			mSelectComputersStatement = mInitStatement(REQ_SELECT_COMPUTERS_FILENAME, mDbConn);
			mCountComputersStatement = mInitStatement(REQ_COUNT_COMPUTERS_FILENAME, mDbConn);
			mInsertComputerStatement = mInitStatement(REQ_INSERT_COMPUTER, mDbConn);
			mUpdateComputerStatement = mInitStatement(REQ_UPDATE_COMPUTER, mDbConn);
			mDeleteComputerStatement = mInitStatement(REQ_DELETE_COMPUTER, mDbConn);
	
			//add statements to "to-free" list.
			mStatements.add(mSelectComputersStatement);
			mStatements.add(mCountComputersStatement);
			mStatements.add(mInsertComputerStatement);
			mStatements.add(mUpdateComputerStatement);
			mStatements.add(mDeleteComputerStatement);
		
		} catch (SQLException | IOException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
	}

	private PreparedStatement mInitStatement(String sqlFileName, Connection conn) throws SQLException, IOException {
		File sqlFile = new File(SQL_DIR, sqlFileName);

		FileInputStream fis = null;
		BufferedReader br = null;

		//read file into a string.
		fis = new FileInputStream(sqlFile);
		br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

		StringBuilder strBuilder = new StringBuilder();
		String line;
		while(( line = br.readLine()) != null ) {
			strBuilder.append(line);
			strBuilder.append(' ');
		}
		
		//convert string into a preparedStatement
		PreparedStatement statement = conn.prepareStatement(strBuilder.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
		br.close();
		fis.close();
		return statement;
	}

}
