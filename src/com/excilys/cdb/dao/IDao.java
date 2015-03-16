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
	 * List computers, oder by name.
	 * @param begin order of the computer from which the list begins. Starts from 0.
	 * @param nb count computer to return.. nb=0 for unbound search.
	 * @return the list of computers
	 */
	public List<Computer> listComputersByName(int begin, int nb);
	
	/**
	 * List computers that match the given name.
	 * @param begin order of the computer from which the list begins. Starts from 0.
	 * @param nb count computer to return. List all computers available if this number is negative.
	 * @param case unsensitive name of computers to search for.
	 * @return the list of computers
	 */
	public List<Computer> listComputersByName(int begin, int nb, String name);

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

	public void addComputers(List<Computer> computer);
	
	public void updateComputer(Computer computer);
	
	public void deleteComputer(Computer computer);

	public void deleteComputers(List<Computer> computer);

}
