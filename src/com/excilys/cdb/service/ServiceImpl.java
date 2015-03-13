package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.dao.IDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


/**
 * 
 * @author Nicolas THIERION
 * @version 0.1.0
 */
public class ServiceImpl implements Service {

	/* ***
	 * ATTRIBUTES
	 */
	private IDao mDao;
	
	
	public ServiceImpl(IDao dao) {
		mDao = dao;
	}
	
	
	@Override
	public List<Computer> listComputersByName(int begin, int count) {
		return mDao.listComputersByName(begin, count);
	}

	@Override
	public List<Company> listCompaniesByName(int begin, int nb) {
		return mDao.listCompaniesByName(begin, nb);
	}

	@Override
	public void addComputer(Computer computer) {
		mDao.addComputer(computer);
	}

	@Override
	public void updateComputer(Computer computer) {
		mDao.updateComputer(computer);
	}

	@Override
	public void deleteComputer(Computer computer) {
		mDao.deleteComputer(computer);
	}

}
