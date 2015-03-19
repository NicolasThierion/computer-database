package com.excilys.cdb.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.excilys.cdb.dao.DaoException;
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.dao.mysql.ComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Test methods from ComputerDao.
 * @author Nicolas THIERION.
 *
 */
public class CompanyDaoTest {

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

        //test list construction of various size
        for (final int count : new int[]{2, mComputerDao.getCount(), 0}) {
            final List<Computer> list = mComputerDao.listByName(0, count);
            Assert.assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        mComputerDao.listByName(mComputerDao.getCount(), mComputerDao.getCount());

        final int count = mComputerDao.getCount();
        final List<Computer> list = mComputerDao.listByName(0, count + 2);
        Assert.assertTrue(list.size() == count);
    }

    /**
     * Test of ComputerDao.listByName(String likeName).
     */
    @Test
    @Ignore("not implemented")
    public final void listLikeName() {

        //add one computer
        String name = "Unique computer [" + java.time.Clock.systemUTC().millis() + "]";
        final Computer computer = new Computer(name);
        mComputerDao.add(computer);

        List<Computer> list = mComputerDao.listLikeName(0, -1, name);

        Assert.assertTrue(list.size() == 1);
        mComputerDao.delete(computer);

        name = "I don't exist";
        list = mComputerDao.listLikeName(0, 0, name);
        Assert.assertTrue(list.size() == 0);
    }


    @Test
    @Ignore("not implemented")
    public final void testCountComputers() {
        mComputerDao.getCount();
    }


    @Test
    @Ignore("not implemented")
    public final void testAddComputer() {
        //add a computer with NULL fields.
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerDao.add(computer);
    }

    @Test (expected = DaoException.class)
    @Ignore("not implemented")
    public final void testAddComputerTwice() {
        //add the same computer twice
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerDao.add(computer);
        mComputerDao.add(computer);
    }

    @Test
    @Ignore("not implemented")
    public final void testAddWrongComputer() {

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

        //try an invalid id
        try {
            mComputerDao.add(new Computer(-1, compName));
            passed = true;
        } catch (IllegalArgumentException | DaoException e) {
        }

        //try an invalid company
        try {
            mComputerDao.add(new Computer(compName, wrongCompany, null, null));
            passed = true;
        } catch (IllegalArgumentException | DaoException e) {
        }

        Assert.assertFalse(passed);
    }


    @Test
    @Ignore("not implemented")
    public final void testDeleteComputer() {
        final String computerName = "Surface pro 4";
        final int count = mComputerDao.listLikeName(0, 0, computerName).size();

        final Computer computer = new Computer(computerName);
        mComputerDao.add(computer);
        mComputerDao.delete(computer);

        Assert.assertTrue(mComputerDao.listLikeName(0, 0, computerName).size() == count);

        boolean passed = false;
        try {
            mComputerDao.delete(computer);
            passed = true;
        } catch (final DaoException e) {
        }

        Assert.assertFalse(passed);
    }

    @Test
    @Ignore("not implemented")
    public final void testUpdateComputer() {
        final String computerName = "Surface pro 4";
        final String computerName2 = "Unique computer";
        List<Computer> list = mComputerDao.listLikeName(0, -1, computerName);
        final int count = mComputerDao.listLikeName(0, -1, computerName).size();

        //add a new computer
        final Computer computer = new Computer(computerName2);
        mComputerDao.add(computer);

        //update its name.
        computer.setName(computerName);
        mComputerDao.update(computer);

        list = mComputerDao.listLikeName(0, -1, computerName2);
        Assert.assertTrue(list.size() == 0);

        list = mComputerDao.listLikeName(0, -1, computerName);
        Assert.assertTrue(list.size() == count + 1);

        mComputerDao.delete(computer);

        boolean passed = false;

        //try updating non-existing computer.
        try {
            mComputerDao.update(computer);
            passed = true;
        } catch (final DaoException e) {
        }

        //try updating invalid computer.
        try {
            mComputerDao.update(new Computer());
            passed = true;
        } catch (final DaoException e) {
        }

        Assert.assertFalse(passed);

    }

}
