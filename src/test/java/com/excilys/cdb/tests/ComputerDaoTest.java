package com.excilys.cdb.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.dao.DaoException;
import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.dao.mysql.CompanyDao;
import com.excilys.cdb.dao.mysql.ComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Unit test for ComputerDao methods.
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
public class ComputerDaoTest {

    /** Computer Dao used for this tests. */
    private IComputerDao mComputerDao;

    /**
     * init Computer & Company Dao.
     */
    @Before
    public final void init() {
        mComputerDao = ComputerDao.getInstance();
    }

    /**
     * Test of ComputerDao.listByName().
     */
    @Test
    public final void listByName() {

        List<Computer> list;
        //test list construction of various size
        for (int count : new int[]{2, mComputerDao.getCount(), 0}) {
            list = mComputerDao.listByName(0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mComputerDao.listByName(mComputerDao.getCount(), mComputerDao.getCount());
        assertTrue(list.size() == 0);

        int count = mComputerDao.getCount();
        list = mComputerDao.listByName(0, count + 2);
        assertTrue(list.size() == count);

        //negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mComputerDao.listByName(invalidOffset, count + 2);
        } catch (IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    /**
     * Test of ComputerDao.listByName(String likeName).
     */
    @Test
    public final void listLikeName() {

        //add one computer
        final String uniqueName = "Unique computer [" + java.time.Clock.systemUTC().millis() + "]";
        final String nonExistantName = "I don't exist";

        Computer uniqueComputer = new Computer(uniqueName);
        mComputerDao.add(uniqueComputer);

        List<Computer> list = mComputerDao.listLikeName(0, -1, uniqueName);

        assertTrue(list.size() == 1);
        mComputerDao.delete(uniqueComputer);

        list = mComputerDao.listLikeName(0, 0, nonExistantName);
        assertTrue(list.size() == 0);
    }

    /**
     * Test of ComputerDao.getCount().
     */
    @Test
    public final void getCount() {
        mComputerDao.getCount();
    }

    /**
     * Test of ComputerDao.add(Computer computer).
     */
    @Test
    public final void add() {
        //add a computer with NULL fields.
        final String compName = "Surface pro 3";
        Computer computer = new Computer(compName);
        mComputerDao.add(computer);
    }

    @Test (expected = DaoException.class)
    public final void testAddComputerTwice() {
        //add the same computer twice
        final String compName = "Surface pro 3";
        Computer computer = new Computer(compName);
        mComputerDao.add(computer);
        mComputerDao.add(computer);
    }

    @Test
    public final void testAddWrongComputer() {

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

        assertFalse(passed);
    }


    @Test
    public final void testDeleteComputer() {
        String computerName = "Surface pro 4";
        int count = mComputerDao.listLikeName(0, 0, computerName).size();

        Computer computer = new Computer(computerName);
        mComputerDao.add(computer);
        mComputerDao.delete(computer);

        assertTrue(mComputerDao.listLikeName(0, 0, computerName).size() == count);

        boolean passed = false;
        try {
            mComputerDao.delete(computer);
            passed = true;
        }
        catch (DaoException e) {}

        assertFalse(passed);		
    }

    @Test
    public final void testUpdateComputer() {
        String computerName = "Surface pro 4";
        String computerName2 = "Unique computer";
        List<Computer> list = mComputerDao.listLikeName(0, -1, computerName);
        int count = mComputerDao.listLikeName(0, -1, computerName).size();

        //add a new computer
        Computer computer = new Computer(computerName2);
        mComputerDao.add(computer);

        //update its name.
        computer.setName(computerName);
        mComputerDao.update(computer);

        list = mComputerDao.listLikeName(0, -1, computerName2);
        assertTrue(list.size() == 0);

        list = mComputerDao.listLikeName(0, -1, computerName);
        assertTrue(list.size() == count + 1);

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

        assertFalse(passed);	

    }

}
