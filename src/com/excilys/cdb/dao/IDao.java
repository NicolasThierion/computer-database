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
	 * @param nb number of computer to return.. nb=0 for unbound search.
	 * @return the list of computers
	 */
	public List<Computer> listComputersByName(int begin, int nb);
	
	/**
	 * List computers that match the given name.
	 * @param begin order of the computer from which the list begins. Starts from 0.
	 * @param nb number of computer to return. List all computers available if this number is negative.
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

	/**
	 * List companies, order by company name.
	 * @param begin 
	 * @param nb of companies to list. List all companies available if negative.
	 * @return
	 */
	public List<Company> listCompaniesByName(int begin, int nb);
	
	/**
	 * Adds the computer to DB, provided that this computer doesn't exist already.
	 * @param computer Computer to add to DB.
	 */
	public void addComputer(Computer computer);

	/**
	 * Adds all computers given in parameters, provided that computers don't exists already.
	 * @param computers Computers to add to DB.
	 */
	public void addComputers(List<Computer> computers);
	
	/**
	 * Update the given computer.
	 * @param computer Computer to update.
	 */
	public void updateComputer(Computer computer);
	
	/**
	 * Delete the given compute from DB.
	 * @param computer
	 */
	public void deleteComputer(Computer computer);

	/**
	 * Delete the given computers from DB.
	 * @param computers
	 */
	public void deleteComputers(List<Computer> computers);

}
