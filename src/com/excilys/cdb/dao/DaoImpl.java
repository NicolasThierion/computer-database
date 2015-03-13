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
	private static final String DB_OPTIONS 			= "zeroDateTimeBehavior=convertToNull";	
	private static final String DB_URL = "jdbc:mysql://"
			+ DB_HOST + "/" + DB_NAME 
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
	private static final String REQ_SELECT_COMPANIES_FILENAME = "select_computers_paging.sql";
	private static final String REQ_COUNT_COMPANIES_FILENAME = "select_company_count.sql";

	/* ***
	 * PREPARED STATEMENTS
	 */
	private List<PreparedStatement> mStatements;
	private PreparedStatement mSelectComputersStatement;
	private PreparedStatement mCountComputersStatement;
	private PreparedStatement mSelectCompaniesStatement;
	private PreparedStatement mCountCompaniesStatement;

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

		List<Computer> resList = new LinkedList<Computer>();

		//ensure init has been called (& thus we have the needed preparedStatement)
		if(!isInitialized()) {
			throw new DaoException("Dao not initialized (forgot to call \"init()\" ?)", ErrorType.DAO_ERROR);
		}
		try {
			//set range parameters
			mSelectComputersStatement.setInt(1, begin);
			mSelectComputersStatement.setInt(2, nb);

			//exec query
			ResultSet res = mSelectComputersStatement.executeQuery();

			HashMap<Integer, Company> companiesMap = new HashMap<Integer, Company>();

			//parse resultSet to build the list of computers.
			while(res.next()) {

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
				Company company;

				//store used companies in a map in order to not construct same company twice.
				if(!companiesMap.containsKey(new Integer(compId))) {
					company = new Company(compId, compName);
					companiesMap.put(new Integer(compId), company);
				}
				else {
					company = companiesMap.get(new Integer(compId));
				}

				//finally, create the computer & add it to the list
				Computer computer = new Computer(id, name);
				computer.setCompany(company);
				computer.setReleaseDate(released);
				computer.setEndingDate(ended);
				resList.add(computer);
			}

		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
		return resList;
	}

	@Override
	public int getComputerCount() {
		return getXXXCount(mCountComputersStatement);
	}

	@Override
	public int getCompanyCount() {
		return getXXXCount(mCountCompaniesStatement);
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
			mSelectComputersStatement.setInt(1, begin);
			mSelectComputersStatement.setInt(2, nb);

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

	@Override
	public void addComputer(Computer computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateComputer(Computer computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteComputer(Computer computer) {
		// TODO Auto-generated method stub

	}

	/* ***
	 * ACCESSORS
	 */
	public boolean isInitialized() {
		return mDbConn != null;
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



		mStatements.add(mSelectComputersStatement);
		mStatements.add(mCountComputersStatement);
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
		PreparedStatement statement = mDbConn.prepareStatement(strBuilder.toString());
		br.close();
		fis.close();
		return statement;
	}

	private int getXXXCount(PreparedStatement statement) {
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
}
