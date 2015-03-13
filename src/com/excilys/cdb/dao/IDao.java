package com.excilys.cdb.dao;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


/**
 * Data access object interface. Establish connection to BDD, & offers several services.
 * 
 * @author Nicolas THIERION
 * @version 0.1.0 
 */
public interface IDao {
	
	public void destroy();

	public void init();
	
	/**
	 * List computers by name
	 * @param begin order of the computer from which the list begins. Starts from 0.
	 * @param count count computer to return.
	 * @return the list of computers
	 */
	public List<Computer> listComputersByName(int begin, int count);

	/**
	 * 
	 * @return count of computer entries in database
	 */
	public int getComputerCount();

	/**
	 * 
	 * @return count of company entries in database
	 */
	public int getCompanyCount();

	
	public List<Company> listCompaniesByName(int begin, int nb);
	
	public void addComputer(Computer computer);
	
	public void updateComputer(Computer computer);
	
	public void deleteComputer(Computer computer);
}
