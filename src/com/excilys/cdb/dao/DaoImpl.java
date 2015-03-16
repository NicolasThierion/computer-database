package com.excilys.cdb.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.excilys.cdb.dao.DaoException.ErrorType;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


/**
 * Data access object singleton. Establish connection to BDD, & offers several services.
 * Use required creditentials to connect to computer-database-db through JDBC MySQL.
 * 
 * 
 * @author Nicolas THIERION
 * @version 0.1.0 
 */
public final class DaoImpl implements IDao {

	/* ***
	 * DB PARAMETERS 
	 */
	private static final String DB_USER 			= "admincdb";
	private static final String DB_PASSWD 			= "qwerty1234";
	private static final String DB_DRIVER_PACKAGE 	= "com.mysql.jdbc.Driver";
	private static final String DB_NAME 			= "computer-database-db";
	private static final String DB_HOST 			= "localhost";
	private static final String DB_PORT				= "3306";
	private static final String DB_OPTIONS 			= "zeroDateTimeBehavior=convertToNull";	
	private static final String DB_URL = "jdbc:mysql://"
			+ DB_HOST + ":" + DB_PORT + "/" + DB_NAME 
			+ "?user=" + DB_USER + "&password=" + DB_PASSWD
			+ "&" + DB_OPTIONS;


	/* ***
	 * DB REQUESTS
	 * ***/
	/** where SQL scripts are stored */
	private static final File SQL_DIR = new File("res/sql");

	/** various sql script usetr to build preparedStatements */
	private static final String REQ_SELECT_COMPUTERS_FILENAME = "select_computers_paging.sql";
	private static final String REQ_COUNT_COMPUTERS_FILENAME = "select_computer_count.sql";
	private static final String REQ_SELECT_COMPANIES_FILENAME = "select_companies_paging.sql";
	private static final String REQ_COUNT_COMPANIES_FILENAME = "select_company_count.sql";
	private static final String REQ_UPDATE_COMPUTER = "update_computer.sql";
	private static final String REQ_INSERT_COMPUTER = "insert_computer.sql";
	private static final String REQ_DELETE_COMPUTER = "delete_computer_with_id.sql";

	/* ***
	 * PREPARED STATEMENTS
	 */
	private List<PreparedStatement> mStatements;
	private PreparedStatement mSelectComputersStatement;
	private PreparedStatement mCountComputersStatement;
	private PreparedStatement mSelectCompaniesStatement;
	private PreparedStatement mCountCompaniesStatement;
	private PreparedStatement mInsertComputerStatement;
	private PreparedStatement mUpdateComputerStatement;
	private PreparedStatement mDeleteComputerStatement;

	/* ***
	 * ATTRIBUTES
	 */
	/** Singleton's instance */
	private static DaoImpl mInstance = null;

	/** unique DB connection */
	private Connection mDbConn;

	/* ***
	 * CONSTRUCTORS / DESTRUCTORS
	 */
	/**
	 * Init the DAO layer. Create a unique connection.
	 * Has no effect when called twice.
	 * @throws DaoException if cannot establish connection to DB.
	 */
	public void init() throws DaoException {
		try {
			if(mDbConn != null && !mDbConn.isClosed())
				return;

			mDbConn = DriverManager.getConnection (DB_URL);
			mInitStatements();
		} catch(SQLException | IOException e) {
			//something went wrong... Cleanup & set dbConn to null.
			destroy();
			mDbConn = null;
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
	}


	/**
	 * 
	 * @return unique instance of DAO.
	 */
	public static DaoImpl getInstance() {
		synchronized(DaoImpl.class) {
			if(mInstance == null) {
				mInstance = new DaoImpl();
			}
		}
		return mInstance;
	}

	/**
	 * Init SQL Driver once.
	 */
	private DaoImpl() {

		//init sql drivers
		try {
			Class.forName(DB_DRIVER_PACKAGE).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
		}
	}

	/**
	 * Close the connection to the DB & free up memory.
	 */
	public void destroy() {
		if(mDbConn != null) {
			try {
				mDbConn.close();
				for(PreparedStatement statement : mStatements) {
					if(statement != null) {
						statement.close();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
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
	public List<Computer> listComputersByName(int begin, int nb) throws DaoException {

		String name = "%%";
		return listComputersByName(begin, nb, name);
	}


	@Override
	public List<Computer> listComputersByName(int begin, int nb, String name) {
		//ensure init has been called (& thus we have the needed preparedStatement)
		if(!isInitialized()) {
			throw new DaoException("Dao not initialized (forgot to call \"init()\" ?)", ErrorType.DAO_ERROR);
		}
		try {
			begin = (begin<0 ? 0 : begin);
			nb = (nb<0 ? Integer.MAX_VALUE : nb);

			name = (name==null || name.isEmpty() ? "%%" : "%" + name + "%");
			name = name.toUpperCase();
			//set range parameters
			mSelectComputersStatement.setString(1, name);
			mSelectComputersStatement.setInt(2, begin);
			mSelectComputersStatement.setInt(3, nb);

		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}

		return mListComputers(mSelectComputersStatement);	
	}


	@Override
	public void addComputers(List<Computer> computer) {
		// TODO Auto-generated method stub

	}


	@Override
	public int getComputerCount() {
		//ensure init has been called (& thus we have the needed preparedStatement)
		if(!isInitialized()) {
			throw new DaoException("Dao not initialized (forgot to call \"init()\" ?)", ErrorType.DAO_ERROR);
		}

		return mGetXXXCount(mCountComputersStatement);
	}

	@Override
	public int getCompanyCount() {
		//ensure init has been called (& thus we have the needed preparedStatement)
		if(!isInitialized()) {
			throw new DaoException("Dao not initialized (forgot to call \"init()\" ?)", ErrorType.DAO_ERROR);
		}

		return mGetXXXCount(mCountCompaniesStatement);
	}

	@Override
	public List<Company> listCompaniesByName(int begin, int nb) {
		List<Company> resList = new LinkedList<Company>();

		//ensure init has been called (& thus we have the needed preparedStatement)
		if(!isInitialized()) {
			throw new DaoException("Dao not initialized (forgot to call \"init()\" ?)", ErrorType.DAO_ERROR);
		}
		try {
			//set range parameters
			mSelectCompaniesStatement.setInt(1, begin);
			mSelectCompaniesStatement.setInt(2, nb);

			//exec query
			ResultSet res = mSelectCompaniesStatement.executeQuery();

			//parse resultSet to build the list of computers.
			while(res.next()) {

				//get company name;
				int compId = res.getInt("id");
				String compName = res.getString("name");
				Company company = new Company(compId, compName);
				resList.add(company);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
		return resList;
	}

	/**
	 * Calling this method will assign a new "computer id" to the computer.
	 * @throw DaoException when trying to add an invalid computer. An invalid computer is a computer with a non blank field "Computer id".
	 * See "updateComputer()" if you want to update computer information of an existing computer.
	 */
	@Override
	public void addComputer(Computer computer) throws DaoException {
		//ensure init has been called (& thus we have the needed preparedStatement)
		if(!isInitialized()) {
			throw new DaoException("Dao not initialized (forgot to call \"init()\" ?)", ErrorType.DAO_ERROR);
		}

		try {
			//ensure that we are attempting to add a NEW computer (with id field = null)"
			Integer id = computer.getId();
			if(id != null ) {
				throw new DaoException("Trying to add a computer : " + computer + " with non-blank field \"computer id\"", ErrorType.SQL_ERROR);
			}

			//build SQL request : 
			// INSERT INTO computer (name, introduced, discontinued, company_id)
			//get computer properties

			ComputerPropertiesHolder h = new ComputerPropertiesHolder();
			h.fromComputer(computer);

			//TODO remove
			/*
			String name = computer.getName();
			Date introDate = computer.getIntroDate();
			Date discDate = computer.getDiscontDate();
			Integer companyId = computer.getCompany().getId();	

			//convert java Date to sql Timestamp, ensuring date is not null
			java.sql.Timestamp introSqlTime, discSqlTime;
			introSqlTime = (introDate!=null ? new java.sql.Timestamp(introDate.getTime()) : null);
			discSqlTime = (discDate!=null ? new java.sql.Timestamp(discDate.getTime()) : null);
			 */
			mInsertComputerStatement.setString(1, h.name);
			mInsertComputerStatement.setTimestamp(2, h.sqlReleaseDate);
			mInsertComputerStatement.setTimestamp(3, h.sqlDiscDate);

			if(h.companyId == null) {
				mInsertComputerStatement.setNull(4, java.sql.Types.INTEGER);
			}
			else {
				mInsertComputerStatement.setInt(4, h.companyId);
			}		 
			int res = mInsertComputerStatement.executeUpdate();
			if(res != 1) {
				throw new DaoException("Something went wrong when adding computer " + computer + ". No changes commited.", ErrorType.SQL_ERROR);
			}

			//get generated id...
			ResultSet rs = mInsertComputerStatement.getGeneratedKeys();
			if (rs.next()){
				//& update this computer with new generated id.
				computer.setId(rs.getInt(1));
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
	public void updateComputer(Computer computer) {
		//Delete computer by id : ensure computer has id != null
		if(computer.getId() == null)
			throw new DaoException("Computer id is null. Cannot update this computer", ErrorType.DAO_ERROR);

		try {
			//retrieve computer information
			ComputerPropertiesHolder h = new ComputerPropertiesHolder();
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
				mUpdateComputerStatement.setInt(4, h.companyId);
			}
			mUpdateComputerStatement.setInt(5, h.id);
			int res = mUpdateComputerStatement.executeUpdate();

			//ensure everything is OK
			if(res != 1) {
				throw new DaoException("Something went wrong while deleting compuer " + computer + ". Maybe this computer doesn't exist?", 
						ErrorType.SQL_ERROR);
			}

		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}	
	}

	/**
	 * Delete the given computer from DB.
	 * @throws DaoException if deletion failed or if provided computer is invalid or doesn't exist.
	 */
	@Override
	public void deleteComputer(Computer computer) throws DaoException{

		//Delete computer by id : ensure computer has id != null
		if(computer.getId() == null)
			throw new DaoException("Computer id is null. Cannot delete this computer", ErrorType.DAO_ERROR);

		try {
			mDeleteComputerStatement.setInt(1, computer.getId());
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

	@Override
	public void deleteComputers(List<Computer> computer) {
		// TODO Auto-generated method stub

	}

	/* ***
	 * ACCESSORS
	 */
	public boolean isInitialized() {
		return mDbConn != null;
	}

	/* ***
	 * PRIVATE CLASS
	 */
	private class ComputerPropertiesHolder {

		/** name of this computer */
		public String name;
		/** manufacturer of this computer */
		public Company company;
		/** release date */
		public Date releaseDate;
		/** discontinuation date */
		public Date discDate;
		/** id of this computer. Remember to test if not null!!!! */
		public Integer id;

		public java.sql.Timestamp sqlReleaseDate;
		public java.sql.Timestamp sqlDiscDate;
		/** remember to test if not null!!!! */
		public Integer companyId;
		public String companyName;


		public void fromComputer(Computer computer) {
			id = computer.getId();
			name = computer.getName();
			releaseDate = computer.getIntroDate();
			discDate = computer.getDiscontDate();
			company = computer.getCompany();
			if(company != null) {
				companyId = computer.getCompany().getId();
				companyName = company.getName();
			}
			
			
			//convert java Date to sql Timestamp, ensuring date is not null
			sqlReleaseDate = (releaseDate!=null ? new java.sql.Timestamp(releaseDate.getTime()) : null);
			sqlDiscDate = (sqlDiscDate!=null ? new java.sql.Timestamp(sqlDiscDate.getTime()) : null);
		}

		public void fromResultSet(ResultSet res) throws SQLException {
			id = res.getInt(1);
			name = res.getString(2);

			sqlReleaseDate = res.getTimestamp(3);
			sqlDiscDate = res.getTimestamp(4);

			//convert sql.Date to java.util.Date, asserting date is not null.
			releaseDate = (sqlReleaseDate!=null? new Date(sqlReleaseDate.getTime()) : null);
			discDate = (sqlDiscDate!=null? new Date(sqlDiscDate.getTime()) : null);

			//get company name;
			companyId = res.getInt(5);
			companyName= res.getString(6);
		}
	}



	/* ***
	 * PRIVATE METHODS
	 */

	/**
	 * init prepared statement used for various DAO services.
	 * @throws SQLException
	 * @throws IOException 
	 */
	private void mInitStatements() throws SQLException, IOException {

		mStatements = new LinkedList<PreparedStatement>();
		mSelectComputersStatement = mInitStatement(REQ_SELECT_COMPUTERS_FILENAME);
		mCountComputersStatement = mInitStatement(REQ_COUNT_COMPUTERS_FILENAME);
		mSelectCompaniesStatement = mInitStatement(REQ_SELECT_COMPANIES_FILENAME);
		mCountCompaniesStatement = mInitStatement(REQ_COUNT_COMPANIES_FILENAME);
		mInsertComputerStatement = mInitStatement(REQ_INSERT_COMPUTER);
		mUpdateComputerStatement = mInitStatement(REQ_UPDATE_COMPUTER);
		mDeleteComputerStatement = mInitStatement(REQ_DELETE_COMPUTER);

		//add statements to "to-free" list.
		mStatements.add(mSelectComputersStatement);
		mStatements.add(mCountComputersStatement);
		mStatements.add(mSelectCompaniesStatement);
		mStatements.add(mCountCompaniesStatement);
		mStatements.add(mInsertComputerStatement);
		mStatements.add(mUpdateComputerStatement);
		mStatements.add(mDeleteComputerStatement);
	}

	private PreparedStatement mInitStatement(String sqlFileName) throws SQLException, IOException {
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
		PreparedStatement statement = mDbConn.prepareStatement(strBuilder.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
		br.close();
		fis.close();
		return statement;
	}

	private int mGetXXXCount(PreparedStatement statement) {
		int count = 0;
		try {
			ResultSet res = statement.executeQuery();
			while(res.next()) {
				count = res.getInt(1);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
		return count;
	}

	private List<Computer> mListComputers(PreparedStatement statement) {
		List<Computer> resList = new LinkedList<Computer>();

		try {
			//exec query
			ResultSet res = statement.executeQuery();

			HashMap<Integer, Company> companiesMap = new HashMap<Integer, Company>();

			//parse resultSet to build the list of computers.
			while(res.next()) {

				//TODO remove
				/*
				int id = res.getInt("id");
				String name = res.getString("name");
				java.sql.Date sqlDateR, sqlDateE;

				sqlDateR = res.getDate("introduced");
				sqlDateE = res.getDate("discontinued");

				//convert sql.Date to java.util.Date, asserting date is not null.
				Date released = (sqlDateR!=null? new Date(sqlDateR.getTime()) : null);
				Date ended = (sqlDateE!=null? new Date(sqlDateE.getTime()) : null);

				//get company name;
				int compId = res.getInt("company_id");
				String compName = res.getString("compName");
				 */
				ComputerPropertiesHolder h = new ComputerPropertiesHolder();
				h.fromResultSet(res);

				Company company;

				//store used companies in a map in order to not construct same company twice.
				if(!companiesMap.containsKey(new Integer(h.companyId))) {
					company = new Company(h.companyId, h.companyName);
					companiesMap.put(new Integer(h.companyId), company);
				}
				else {
					company = companiesMap.get(new Integer(h.companyId));
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

}
