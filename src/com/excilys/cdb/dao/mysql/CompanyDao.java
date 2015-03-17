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
import java.util.LinkedList;
import java.util.List;

import com.excilys.cdb.dao.ConnectionFactory;
import com.excilys.cdb.dao.DaoException;
import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.dao.DaoException.ErrorType;
import com.excilys.cdb.model.Company;


/**
 * 
 * 
 * @author Nicolas THIERION
 * @version 0.2.0 
 */
public class CompanyDao implements ICompanyDao {

	/* ***
	 * DB REQUESTS
	 * ***/
	/** where SQL scripts are stored */
	private static final File SQL_DIR = new File("res/sql");

	/** various sql script user to build preparedStatements */
	private static final String REQ_SELECT_COMPANIES_FILENAME = "select_companies_paging.sql";
	private static final String REQ_COUNT_COMPANIES_FILENAME = "select_company_count.sql";

	/* ***
	 * PREPARED STATEMENTS
	 */
	private List<PreparedStatement> mStatements;
	private PreparedStatement mSelectCompaniesStatement;
	private PreparedStatement mCountCompaniesStatement;

	/* ***
	 * ATTRIBUTES
	 */
	/** Singleton's instance */
	private static CompanyDao mInstance = null;

	/** unique DB connection */
	private Connection mDbConn = ConnectionFactory.getInstance().getConnection();

	/* ***
	 * CONSTRUCTORS / DESTRUCTORS
	 */
	private CompanyDao() {
		mInitStatements();
	}
	
	/**
	 * 
	 * @return unique instance of DAO.
	 */
	public static CompanyDao getInstance() {
		synchronized(CompanyDao.class) {
			if(mInstance == null) {
				mInstance = new CompanyDao();
			}
		}
		return mInstance;
	}

	/**
	 * free up memory & clise resources
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
	public List<Company> listByName(int begin, int nb) throws DaoException {

		String name = "%%";
		return listByName(begin, nb, name);
	}


	@Override
	public List<Company> listByName(int begin, int nb, String name) {
		
		try {
			begin = (begin<0 ? 0 : begin);
			nb = (nb<0 ? Integer.MAX_VALUE : nb);

			name = name.toUpperCase();
			//set range parameters
			mSelectCompaniesStatement.setString(1, name);
			mSelectCompaniesStatement.setInt(2, begin);
			mSelectCompaniesStatement.setInt(3, nb);

			List<Company> resList = new LinkedList<Company>();

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
			
			return resList;

			
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
	}

	@Override
	public int getCount() {
		
		int count = 0;
		try {
			ResultSet res = mCountCompaniesStatement.executeQuery();
			while(res.next()) {
				count = res.getInt(1);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), DaoException.ErrorType.SQL_ERROR);
		}
		return count;
	}



	


	/* ***
	 * PRIVATE METHODS
	 */

	/**
	 * init prepared statement used for various DAO services.
	 */
	private void mInitStatements() {

		try {
			mStatements = new LinkedList<PreparedStatement>();
			mSelectCompaniesStatement = mInitStatement(REQ_SELECT_COMPANIES_FILENAME, mDbConn);
			mCountCompaniesStatement = mInitStatement(REQ_COUNT_COMPANIES_FILENAME, mDbConn);
		
			//add statements to "to-free" list.
			mStatements.add(mSelectCompaniesStatement);
			mStatements.add(mCountCompaniesStatement);
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
