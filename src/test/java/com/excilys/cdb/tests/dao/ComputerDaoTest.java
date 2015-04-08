package com.excilys.cdb.tests.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.ICompanyDao;
import com.excilys.cdb.persistence.dao.IComputerDao;
import com.excilys.cdb.persistence.dao.mysql.CompanyDao;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.persistence.mapper.CompanyMapper;
import com.excilys.cdb.persistence.mapper.ComputerMapper;

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
    public final void testListByName() {

        List<Computer> list;
        //test list construction of various size
        for (final int count : new int[]{2, mComputerDao.getCount(), 0}) {
            list = mComputerDao.listBy(ComputerMapper.Field.NAME, 0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mComputerDao.listBy(ComputerMapper.Field.NAME, mComputerDao.getCount(), mComputerDao.getCount());
        assertTrue(list.size() == 0);

        final int count = mComputerDao.getCount();
        list = mComputerDao.listBy(ComputerMapper.Field.NAME, 0, count + 2);
        assertTrue(list.size() == count);

        //negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mComputerDao.listBy(ComputerMapper.Field.NAME, invalidOffset, count + 2);
        } catch (final IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    /**
     * Test of ComputerDao.listByName(String likeName).
     */
    @Test
    public final void testListLikeName() {

        //add one computer
        final String uniqueName = "Unique computer [" + java.time.Clock.systemUTC().millis() + "]";
        final String nonExistantName = "I don't exist";

        final Computer uniqueComputer = new Computer(uniqueName);
        mComputerDao.add(uniqueComputer);

        List<Computer> list = mComputerDao.listLike(ComputerMapper.Field.NAME, uniqueName, 0, -1);

        assertTrue(list.size() == 1);
        mComputerDao.delete(uniqueComputer);

        list = mComputerDao.listLike(ComputerMapper.Field.NAME, nonExistantName, 0, 0);
        assertTrue(list.size() == 0);
    }

    /**
     * Test of ComputerDao.getCount().
     */
    @Test
    public final void testGetCount() {
        mComputerDao.getCount();
    }

    /**
     * Test of ComputerDao.add(Computer computer).
     */
    @Test
    public final void testAddNull() {
        //add a computer with NULL fields.
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerDao.add(computer);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAddTwice() {
        //add the same computer twice
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerDao.add(computer);
        mComputerDao.add(computer);
    }

    @Test
    public final void testAddWrong() {
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
    public final void testDelete() {
        final String computerName = "Surface pro 4";
        final int count = mComputerDao.listLike(ComputerMapper.Field.NAME, computerName).size();

        final Computer computer = new Computer(computerName);
        mComputerDao.add(computer);
        mComputerDao.delete(computer);

        assertTrue(mComputerDao.listLike(ComputerMapper.Field.NAME, computerName).size() == count);

        boolean passed = false;
        try {
            mComputerDao.delete(computer);
            passed = true;
        } catch (final NoSuchElementException e) {
        }

        assertFalse(passed);
    }

    @Test
    public final void testUpdate() {
        final String computerName = "Surface pro 4";
        final String computerName2 = "Unique computer" + java.time.Clock.systemUTC().millis();
        final LocalDate releaseDate = java.time.LocalDate.of(1999, 1, 1);
        final LocalDate discontinuedDate = java.time.LocalDate.of(2010, 1, 1);
        final Company company = mCompanyDao.searchBy(CompanyMapper.Field.ID, "" + 1L);

        List<Computer> list = mComputerDao.listLike(ComputerMapper.Field.NAME, computerName);
        final int count = mComputerDao.listLike(ComputerMapper.Field.NAME, computerName).size();

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
        list = mComputerDao.listLike(ComputerMapper.Field.NAME, computerName2);
        assertTrue(list.size() == 0);

        list = mComputerDao.listLike(ComputerMapper.Field.NAME, computerName);
        assertTrue(list.size() == count + 1);

        // ensure computer has been updated correctly
        final Computer computerCopy = mComputerDao.searchBy(ComputerMapper.Field.ID, computer.getId().toString());
        assertTrue(computer.equals(computerCopy));
        mComputerDao.delete(computer);

        boolean passed = false;

        // try updating non-existing computer.
        try {
            mComputerDao.update(computer);
            passed = true;
        } catch (final NoSuchElementException | DaoException e) {
        }

        // try updating invalid computer.
        try {
            mComputerDao.update(new Computer());
            passed = true;
        } catch (final IllegalArgumentException e) {
        }

        assertFalse(passed);
    }
}
