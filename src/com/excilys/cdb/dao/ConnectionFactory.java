package com.excilys.cdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dao.DaoException.ErrorType;


/**
 * Data access object singleton. Establish connection to BDD.
 * Use required creditentials to connect to computer-database-db through JDBC MySQL.
 * 
 * 
 * @author Nicolas THIERION
 * @version 0.2.0 
 */
public class ConnectionFactory {


	/* ***
	 * CONNECTION FACTORY PARAMETERS
	 */
	public static final int CONNECTION_POOL_SIZE = 32;

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
	 * ATTRIBUTES
	 */

	/** singleton's instance */
	private static ConnectionFactory mInstance;

	private List<Connection> mConnections;

	/* ***
	 * CONSTRUCTORS / DESTRUCTORS
	 */

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public static ConnectionFactory getInstance() throws DaoException {
		synchronized(ConnectionFactory.class) {
			if(mInstance == null) {
				mInstance = new ConnectionFactory();
			}
		}
		return mInstance;
	}

	/**
	 * 
	 */
	private ConnectionFactory() {
		//init sql drivers
		try {
			mConnections = new ArrayList<Connection>();
			Class.forName(DB_DRIVER_PACKAGE).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new DaoException(e.getMessage(), ErrorType.DAO_ERROR);
		}
	}

	/**
	 * Close the connection to the DB & free up memory.
	 */
	public void destroy() {

		try {
			for(Connection conn : mConnections) {
				conn.close();
			}	
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
	}

	@Override
	public void finalize() {
		destroy();
	}

	/* ***
	 * ACCESSORS
	 */

	/**
	 * @link open()
	 * @return
	 * @throws DaoException
	 */
	public Connection getConnection() throws DaoException {
		return open();
	}
	/**
	 * Get a connection to the BD.
	 * @throws DaoException if cannot establish connection to DB.
	 */
	public Connection open() throws DaoException {
		try {
			Connection conn = DriverManager.getConnection (DB_URL);
			mConnections.add(conn);
			return conn;
		}
		catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
	}

	/**
	 * 
	 * @param conn
	 */
	public void close(Connection conn) {
		try {
			conn.close();
			mConnections.remove(conn);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), ErrorType.SQL_ERROR);
		}
	}

}
