package com.excilys.cdb.tests.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.mysql.CompanyDao;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

/**
 * Unit test for ComputerDao methods.
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
public class ComputerServiceTest {

    /** Computer Service used for this tests. */
    private IComputerService mComputerService;
    private ICompanyService  mCompanyService;

    /**
     * init Computer service.
     */
    @Before
    public final void init() {

        mComputerService = new ComputerService(ComputerDao.getInstance());
        mCompanyService = new CompanyService(CompanyDao.getInstance());
    }

    /**
     * Test of ComputerService.listByName().
     */
    @Test
    public final void testListByName() {

        List<Computer> list;

        //test list construction of various size
        for (final int count : new int[] {2, mComputerService.getCount(), 0}) {
            list = mComputerService.listByName(0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mComputerService.listByName(mComputerService.getCount(), mComputerService.getCount());
        assertTrue(list.size() == 0);

        final int count = mComputerService.getCount();
        list = mComputerService.listByName(0, count + 2);
        assertTrue(list.size() == count);

        //negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mComputerService.listByName(invalidOffset, count + 2);
        } catch (final IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    /**
     * Test of ComputerService.listByName(String likeName).
     */
    @Test
    public final void testListLikeName() {

        //add one computer
        final String uniqueName = "Unique computer [" + java.time.Clock.systemUTC().millis() + "]";
        final String nonExistantName = "I don't exist";

        final Computer uniqueComputer = new Computer(uniqueName);
        mComputerService.add(uniqueComputer);

        List<Computer> list = mComputerService.listLikeName(0, -1, uniqueName);

        assertTrue(list.size() == 1);
        mComputerService.delete(uniqueComputer);

        list = mComputerService.listLikeName(0, 0, nonExistantName);
        assertTrue(list.size() == 0);
    }

    /**
     * Test of ComputerService.getCount().
     */
    @Test
    public final void testGetCount() {
        assertTrue(mComputerService.getCount() > 0);
    }

    /**
     * Test of ComputerService.add(Computer computer).
     */
    @Test
    public final void testAddNull() {
        //add a computer with NULL fields.
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerService.add(computer);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAddTwice() {
        //add the same computer twice
        final String compName = "Surface pro 3";
        final Computer computer = new Computer(compName);
        mComputerService.add(computer);
        mComputerService.add(computer);
    }

    @Test
    public final void testAddWrong() {
        //add a computer with NULL fields.
        final String compName = "Unknown machine";
        final Company wrongCompany = new Company(-1, "Unknown company");
        boolean passed = false;

        //try an invalid id
        try {
            mComputerService.add(new Computer(0, compName));
            passed = true;
        } catch (final IllegalArgumentException e) {
        }
        assertFalse(passed);

        //try an invalid id
        try {
            mComputerService.add(new Computer(-1, compName));
            passed = true;
        } catch (final IllegalArgumentException e) {
        }
        assertFalse(passed);

        //try an invalid company
        try {
            mComputerService.add(new Computer(compName, wrongCompany, null, null));
            passed = true;
        } catch (final IllegalArgumentException e) {
        }
        assertFalse(passed);

    }

    @Test(expected = NoSuchElementException.class)
    public final void testDelete() {
        final String computerName = "Surface pro 4";
        final int count = mComputerService.listLikeName(0, 0, computerName).size();

        final Computer computer = new Computer(computerName);
        mComputerService.add(computer);
        mComputerService.delete(computer);

        assertTrue(mComputerService.listLikeName(0, 0, computerName).size() == count);

        mComputerService.delete(computer);
    }

    /**
     * test computer retrieval.
     */
    @Test
    public final void testRetrieve() {
        final Computer computer = mComputerService.retrieve(1L);
        assertTrue(computer.getName() != null && !computer.getName().trim().isEmpty());
        assertTrue(computer.getId().equals(1L));
    }

    /**
     * test invalid computer retrieval.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testRetrieveWrong() {
        mComputerService.retrieve(-1L);

    }

    /**
     * test non-existing computer retrieval.
     */
    @Test(expected = NoSuchElementException.class)
    public final void testRetrieveNonExistant() {
        final long tooBigComputerId = 9999999L;
        mComputerService.retrieve(tooBigComputerId);
    }

    @Test
    public final void testSearchById() {
        final long tooBigComputerId = 9999999L;

        assertTrue(mComputerService.search(1L) != null);
        assertTrue(mComputerService.search(tooBigComputerId) == null);
    }



    @Test
    public final void testUpdate() {
        final String computerName = "Surface pro 4";
        final String computerName2 = "Unique computer" + java.time.Clock.systemUTC().millis();
        final LocalDate releaseDate = java.time.LocalDate.of(1999, 1, 1);
        final LocalDate discontinuedDate = java.time.LocalDate.of(2010, 1, 1);
        final Company company = mCompanyService.retrieve(1L);

        List<Computer> list = mComputerService.listLikeName(0, -1, computerName);
        final int count = mComputerService.listLikeName(0, -1, computerName).size();

        // add a new computer
        final Computer computer = new Computer(computerName2);
        mComputerService.add(computer);

        // update its properties.
        computer.setName(computerName);
        computer.setReleaseDate(releaseDate);
        computer.setDiscontDate(discontinuedDate);
        computer.setCompany(company);
        mComputerService.update(computer);

        // ensure modification are effective in db.
        list = mComputerService.listLikeName(0, -1, computerName2);
        assertTrue(list.size() == 0);

        list = mComputerService.listLikeName(0, -1, computerName);
        assertTrue(list.size() == count + 1);

        // ensure computer has been updated correctly
        final Computer computerCopy = mComputerService.retrieve(computer.getId());
        assertTrue(computer.equals(computerCopy));
        mComputerService.delete(computer);

        boolean passed = false;

        // try updating non-existing computer.
        try {
            mComputerService.update(computer);
            passed = true;
        } catch (final NoSuchElementException e) {
        }

        // try updating invalid computer.
        try {
            mComputerService.update(new Computer());
            passed = true;
        } catch (final IllegalArgumentException e) {
        }

        assertFalse(passed);
    }
}
