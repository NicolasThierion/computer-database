package com.excilys.cdb.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
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
    private ICompanyDao  mCompanyDao;

    /**
     * init Computer & Company Dao.
     */
    @Before
    public final void init() {
        mComputerDao = ComputerDao.getInstance();
        mCompanyDao = CompanyDao.getInstance();
    }

    /**
     * Test of ComputerDao.listByName().
     */
    @Test
    public final void listByName() {

        List<Computer> list;
        //test list construction of various size
        for (final int count : new int[]{2, mComputerDao.getCount(), 0}) {
            list = mComputerDao.listByName(0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mComputerDao.listByName(mComputerDao.getCount(), mComputerDao.getCount());
        assertTrue(list.size() == 0);

        final int count = mComputerDao.getCount();
        list = mComputerDao.listByName(0, count + 2);
        assertTrue(list.size() == count);

        //negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mComputerDao.listByName(invalidOffset, count + 2);
        } catch (final IllegalArgumentException e) {
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

        final Computer uniqueComputer = new Computer(uniqueName);
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
    public final void addNull() {
        //add a computer with NULL fields.
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerDao.add(computer);
    }

    @Test (expected = DaoException.class)
    public final void addTwice() {
        //add the same computer twice
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerDao.add(computer);
        mComputerDao.add(computer);
    }

    @Test
    public final void addWrong() {
        //add a computer with NULL fields.
        final String compName = "Unknown machine";
        final Company wrongCompany = new Company(-1, "Unknown company");
        boolean passed = false;

        //try an invalid id
        try {
            mComputerDao.add(new Computer(0, compName));
            passed = true;
        } catch (IllegalArgumentException | DaoException e) {
        }
        assertFalse(passed);

        //try an invalid id
        try {
            mComputerDao.add(new Computer(-1, compName));
            passed = true;
        } catch (IllegalArgumentException | DaoException e) {
        }
        assertFalse(passed);

        //try an invalid company
        try {
            mComputerDao.add(new Computer(compName, wrongCompany, null, null));
            passed = true;
        } catch (IllegalArgumentException | DaoException e) {
        }
        assertFalse(passed);

    }

    @Test
    public final void delete() {
        final String computerName = "Surface pro 4";
        final int count = mComputerDao.listLikeName(0, 0, computerName).size();

        final Computer computer = new Computer(computerName);
        mComputerDao.add(computer);
        mComputerDao.delete(computer);

        assertTrue(mComputerDao.listLikeName(0, 0, computerName).size() == count);

        boolean passed = false;
        try {
            mComputerDao.delete(computer);
            passed = true;
        } catch (final DaoException e) {
        }

        assertFalse(passed);
    }

    @Test
    public final void update() {
        final String computerName = "Surface pro 4";
        final String computerName2 = "Unique computer" + java.time.Clock.systemUTC().millis();
        final LocalDate releaseDate = java.time.LocalDate.of(1999, 1, 1);
        final LocalDate discontinuedDate = java.time.LocalDate.of(2010, 1, 1);
        final Company company = mCompanyDao.searchById(1L);

        List<Computer> list = mComputerDao.listLikeName(0, -1, computerName);
        final int count = mComputerDao.listLikeName(0, -1, computerName).size();

        // add a new computer
        final Computer computer = new Computer(computerName2);
        mComputerDao.add(computer);

        // update its properties.
        computer.setName(computerName);
        computer.setReleaseDate(releaseDate);
        computer.setDiscontDate(discontinuedDate);
        computer.setCompany(company);
        mComputerDao.update(computer);

        // ensure modification are effective in db.
        list = mComputerDao.listLikeName(0, -1, computerName2);
        assertTrue(list.size() == 0);

        list = mComputerDao.listLikeName(0, -1, computerName);
        assertTrue(list.size() == count + 1);

        // ensure computer has been updated correctly
        final Computer computerCopy = mComputerDao.searchById(computer.getId());
        assertTrue(computer.equals(computerCopy));
        mComputerDao.delete(computer);

        boolean passed = false;

        // try updating non-existing computer.
        try {
            mComputerDao.update(computer);
            passed = true;
        } catch (final DaoException e) {
        }

        // try updating invalid computer.
        try {
            mComputerDao.update(new Computer());
            passed = true;
        } catch (final DaoException e) {
        }

        assertFalse(passed);
    }
}
