package com.excilys.cdb.tests;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.dao.DaoException;
import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.dao.mysql.CompanyDao;
import com.excilys.cdb.dao.mysql.ComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class DaoTest {

	private IComputerDao mComputerDao;
	private ICompanyDao mCompanyDao;

	@Before
	public void init() {
		mComputerDao = ComputerDao.getInstance();
		mCompanyDao = CompanyDao.getInstance();
	}

	@After
	public void destroy() {

	}


	@Test
	public void testListComputers() {

		//test list construction of various size
		for(int count : new int[]{2, mComputerDao.getCount(), 0}) {
			List<Computer> list = mComputerDao.listByName(0, count);
			Assert.assertTrue(list.size() == count);
		}

		//test list construction out of bounds
		mComputerDao.listByName(mComputerDao.getCount(), mComputerDao.getCount());

		int count = mComputerDao.getCount();
		List<Computer> list = mComputerDao.listByName(0, count+2);
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
		mComputerDao.add(computer);
		
		List<Computer> list = mComputerDao.listByName(0, -1, name);
		
		Assert.assertTrue(list.size() == 1);
		mComputerDao.delete(computer);

		name = "I don't exist";
		list = mComputerDao.listByName(0, 0, name);
		Assert.assertTrue(list.size() == 0);		
	}
	

	@Test
	public void testCountComputers() {
		mComputerDao.getCount();
	}

	@Test
	public void testListCompanies() {

		//test list construction of various size
		for(int count : new int[]{2, mCompanyDao.getCount(), 0}) {
			List<Company> list = mCompanyDao.listByName(0, count);
			Assert.assertTrue(list.size() == count);
		}

		//test list construction out of bounds
		mComputerDao.listByName(mCompanyDao.getCount(), mCompanyDao.getCount());

		int count = mCompanyDao.getCount();
		List<Company> list = mCompanyDao.listByName(0, count+2);
		Assert.assertTrue(list.size() == count);

		/*
		for (Company c : list)
			System.out.println(c);
		 */
	}

	@Test
	public void testCountCompanies() {
		mCompanyDao.getCount();
	}

	@Test
	public void testAddComputer() {
		//add a computer with NULL fields.
		final String compName = "Surface pro 3";
		Computer computer = new Computer(compName);
		mComputerDao.add(computer);
	}

	@Test (expected = DaoException.class)
	public void testAddComputerTwice() {
		//add the same computer twice
		final String compName = "Surface pro 3";
		Computer computer = new Computer(compName);
		mComputerDao.add(computer);
		mComputerDao.add(computer);
	}

	@Test
	public void testAddWrongComputer() {

		//add a computer with NULL fields.
		final String compName = "Unknown machine";
		final Company wrongCompany = new Company(-1, "Unknown company");
		boolean passed = false;
		
		//try an invalid id
		try {
			mComputerDao.add(new Computer(0, compName));
			passed = true;
		}
		catch (IllegalArgumentException | DaoException e) {}
		
		//try an invalid id
		try {
			mComputerDao.add(new Computer(-1, compName));
			passed = true;
		}
		catch (IllegalArgumentException | DaoException e) {}
		
		//try an invalid company
		try {
			mComputerDao.add(new Computer(compName, wrongCompany, null, null));
			passed = true;
		}
		catch (IllegalArgumentException | DaoException e) {}
		
		Assert.assertFalse(passed);
	}


	@Test
	public void testDeleteComputer() {
		String computerName = "Surface pro 4";
		int count = mComputerDao.listByName(0, 0, computerName).size();
		
		Computer computer = new Computer(computerName);
		mComputerDao.add(computer);
		mComputerDao.delete(computer);
		
		Assert.assertTrue(mComputerDao.listByName(0, 0, computerName).size() == count);
		
		boolean passed = false;
		try {
			mComputerDao.delete(computer);
			passed = true;
		}
		catch (DaoException e) {}
		
		Assert.assertFalse(passed);		
	}
	
	@Test
	public void testUpdateComputer() {
		String computerName = "Surface pro 4";
		String computerName2 = "Unique computer";
		List<Computer> list = mComputerDao.listByName(0, -1, computerName);
		int count = mComputerDao.listByName(0, -1, computerName).size();
		
		//add a new computer
		Computer computer = new Computer(computerName2);
		mComputerDao.add(computer);
		
		//update its name.
		computer.setName(computerName);
		mComputerDao.update(computer);
		
		list = mComputerDao.listByName(0, -1, computerName2);
		Assert.assertTrue(list.size() == 0);
		
		list = mComputerDao.listByName(0, -1, computerName);
		Assert.assertTrue(list.size() == count + 1);
		
		mComputerDao.delete(computer);
		
		boolean passed = false;
		
		//try updating non-existing computer.
		try {
			mComputerDao.update(computer);
			passed = true;
		}
		catch (DaoException e) {}
		
		//try updating invalid computer.
		try {
			mComputerDao.update(new Computer());
			passed = true;
		}
		catch (DaoException e) {}
		
		Assert.assertFalse(passed);	
		
	}

}
