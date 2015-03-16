package com.excilys.cdb.tests;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.dao.DaoException;
import com.excilys.cdb.dao.DaoImpl;
import com.excilys.cdb.dao.IDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class DaoTest {

	private IDao mDao;

	@Before
	public void init() {
		mDao = DaoImpl.getInstance();
		mDao.init();
	}

	@After
	public void destroy() {
		mDao.destroy();
	}

	@Test
	public void testInit() {
		mDao.init();
		mDao.init();
	}

	@Test
	public void testDestroy() {
		mDao.destroy();

		mDao.init();
		mDao.destroy();		
	}

	@Test
	public void testListComputers() {

		//test list construction of various size
		for(int count : new int[]{2, mDao.getComputerCount(), 0}) {
			List<Computer> list = mDao.listComputersByName(0, count);
			Assert.assertTrue(list.size() == count);
		}

		//test list construction out of bounds
		mDao.listComputersByName(mDao.getComputerCount(), mDao.getComputerCount());

		int count = mDao.getComputerCount();
		List<Computer> list = mDao.listComputersByName(0, count+2);
		Assert.assertTrue(list.size() == count);

		/*
		for(Computer c: list) {
			System.out.println(c);
		}
		 */
	}
	

	@Test
	public void testListComputersLikeName() {

		//add one computer
		String name = "Unique computer";
		Computer computer = new Computer(name);
		mDao.addComputer(computer);
		
		List<Computer> list = mDao.listComputersByName(0, -1, name);
		
		Assert.assertTrue(list.size() == 1);
		mDao.deleteComputer(computer);

		name = "I don't exist";
		list = mDao.listComputersByName(0, 0, name);
		Assert.assertTrue(list.size() == 0);		
	}
	

	@Test
	public void testCountComputers() {
		mDao.getComputerCount();
	}

	@Test
	public void testListCompanies() {

		//test list construction of various size
		for(int count : new int[]{2, mDao.getCompanyCount(), 0}) {
			List<Company> list = mDao.listCompaniesByName(0, count);
			Assert.assertTrue(list.size() == count);
		}

		//test list construction out of bounds
		mDao.listCompaniesByName(mDao.getCompanyCount(), mDao.getCompanyCount());

		int count = mDao.getCompanyCount();
		List<Company> list = mDao.listCompaniesByName(0, count+2);
		Assert.assertTrue(list.size() == count);

		/*
		for (Company c : list)
			System.out.println(c);
		 */
	}

	@Test
	public void testCountCompanies() {
		mDao.getCompanyCount();
	}

	@Test
	public void testAddComputer() {
		//add a computer with NULL fields.
		final String compName = "Surface pro 3";
		Computer computer = new Computer(compName);
		mDao.addComputer(computer);
	}

	@Test (expected = DaoException.class)
	public void testAddComputerTwice() {
		//add the same computer twice
		final String compName = "Surface pro 3";
		Computer computer = new Computer(compName);
		mDao.addComputer(computer);
		mDao.addComputer(computer);
	}

	@Test
	public void testAddWrongComputer() {

		//add a computer with NULL fields.
		final String compName = "Unknown machine";
		final Company wrongCompany = new Company(-1, "Unknown company");
		boolean passed = false;
		
		//try an invalid id
		try {
			mDao.addComputer(new Computer(0, compName));
			passed = true;
		}
		catch (IllegalArgumentException | DaoException e) {}
		
		//try an invalid id
		try {
			mDao.addComputer(new Computer(-1, compName));
			passed = true;
		}
		catch (IllegalArgumentException | DaoException e) {}
		
		//try an invalid company
		try {
			mDao.addComputer(new Computer(compName, wrongCompany, null, null));
			passed = true;
		}
		catch (IllegalArgumentException | DaoException e) {}
		
		Assert.assertFalse(passed);
	}


	@Test
	public void testDeleteComputer() {
		String computerName = "Surface pro 4";
		int count = mDao.listComputersByName(0, 0, computerName).size();
		
		Computer computer = new Computer(computerName);
		mDao.addComputer(computer);
		mDao.deleteComputer(computer);
		
		Assert.assertTrue(mDao.listComputersByName(0, 0, computerName).size() == count);
		
		boolean passed = false;
		try {
			mDao.deleteComputer(computer);
			passed = true;
		}
		catch (DaoException e) {}
		
		Assert.assertFalse(passed);		
	}
	
	@Test
	public void testUpdateComputer() {
		String computerName = "Surface pro 4";
		String computerName2 = "Unique computer";
		List<Computer> list = mDao.listComputersByName(0, -1, computerName);
		int count = mDao.listComputersByName(0, -1, computerName).size();
		
		//add a new computer
		Computer computer = new Computer(computerName2);
		mDao.addComputer(computer);
		
		//update its name.
		computer.setName(computerName);
		mDao.updateComputer(computer);
		
		list = mDao.listComputersByName(0, -1, computerName2);
		Assert.assertTrue(list.size() == 0);
		
		list = mDao.listComputersByName(0, -1, computerName);
		Assert.assertTrue(list.size() == count + 1);
		
		mDao.deleteComputer(computer);
		
		boolean passed = false;
		
		//try updating non-existing computer.
		try {
			mDao.updateComputer(computer);
			passed = true;
		}
		catch (DaoException e) {}
		
		//try updating invalid computer.
		try {
			mDao.updateComputer(new Computer());
			passed = true;
		}
		catch (DaoException e) {}
		
		Assert.assertFalse(passed);	
		
	}

}
