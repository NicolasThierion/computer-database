package com.excilys.cdb.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.excilys.cdb.dao.DaoImpl;
import com.excilys.cdb.dao.IDao;
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
	}
	
	@Test
	public void testCountComputers() {
		mDao.getComputerCount();
	}

	
	@Ignore("not yet implemented")
	@Test
	public void testAddComputer() {
		fail("Not yet implemented");
	}
	@Ignore("not yet implemented")
	@Test
	
	public void testDeleteComputers() {
		fail("Not yet implemented");
	}
	@Ignore("not yet implemented")
	@Test
	public void testListCompanies() {
		fail("Not yet implemented");
	}	

}
