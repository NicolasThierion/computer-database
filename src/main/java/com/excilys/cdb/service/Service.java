package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * 
 * @author Nicolas THIERION
 * @version 0.1.0
 * 
 * TODO : First shot of service. Must probably be re-designed.
 */
public interface Service {
	
	public List<Computer> listComputersByName(int begin, int count);
	
	public List<Company> listCompaniesByName(int begin, int nb);

	public void addComputer(Computer computer);
	
	public void updateComputer(Computer computer);
	
	public void deleteComputer(Computer computer);
	
}
