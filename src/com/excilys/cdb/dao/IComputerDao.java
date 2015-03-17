package com.excilys.cdb.dao;

import java.util.List;

import com.excilys.cdb.model.Computer;


/**
 * Data access object interface. Establish connection to BDD, & offers several services.
 * 
 * @author Nicolas THIERION
 * @version 0.1.0 
 */
public interface IComputerDao {
	
	
	/**
	 * List computers, oder by name.
	 * @param begin order of the computer from which the list begins. Starts from 0.
	 * @param nb number of computer to return.. nb=0 for unbound search.
	 * @return the list of computers
	 */
	public List<Computer> listByName(int begin, int nb);
	
	/**
	 * List computers that match the given name.
	 * @param begin order of the computer from which the list begins. Starts from 0.
	 * @param nb number of computer to return. List all computers available if this number is negative.
	 * @param case unsensitive name of computers to search for.
	 * @return the list of computers
	 */
	public List<Computer> listByName(int begin, int nb, String name);

	/**
	 * 
	 * @return count of computer entries in database
	 */
	public int getCount();

	/**
	 * Adds the computer to DB, provided that this computer doesn't exist already.
	 * @param computer Computer to add to DB.
	 */
	public void add(Computer computer);
	
	/**
	 * Update the given computer.
	 * @param computer Computer to update.
	 * @return 
	 */
	public Computer update(Computer computer);
	
	/**
	 * Delete the given compute from DB.
	 * @param computer
	 */
	public void delete(Computer computer);
}
