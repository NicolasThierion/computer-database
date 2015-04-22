package com.excilys.cdb.tests.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
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
public class CompanyServiceTest {

    /** Computer Service used for this tests. */
    private IComputerService mComputerService;
    private ICompanyService  mCompanyService;

    /**
     * init Computer service.
     */
    @Before
    public final void init() {

        mComputerService = new ComputerService(ComputerDao.getInstance());
        mCompanyService = new CompanyService(CompanyDao.getInstance(), ComputerDao.getInstance());
    }

    /**
     * Test of CompanyService.listByName().
     */
    @Test
    public final void testListByName() {

        List<Company> list;

        //test list construction of various size
        for (final int count : new int[] {2, mCompanyService.getCount(), 0}) {
            list = mCompanyService.listByName(0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mCompanyService.listByName(mCompanyService.getCount(), mCompanyService.getCount());
        assertTrue(list.size() == 0);

        final int count = mCompanyService.getCount();
        list = mCompanyService.listByName(0, count + 2);
        assertTrue(list.size() == count);

        //negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mCompanyService.listByName(invalidOffset, count + 2);
        } catch (final IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    /**
     * Test of CompanyService.listByName(String likeName).
     */
    @Test
    public final void testListLikeName() {

        // add one company
        final String uniqueName = "Unique company [" + java.time.Clock.systemUTC().millis() + "]";
        final String nonExistantName = "I don't exist";

        final Company uniqueCompany = new Company(uniqueName);
        mCompanyService.add(uniqueCompany);

        List<Company> list = mCompanyService.listLikeName(uniqueName);

        assertTrue(list.size() == 1);
        mCompanyService.delete(uniqueCompany);

        list = mCompanyService.listLikeName(0, 0, nonExistantName);
        assertTrue(list.size() == 0);
    }

    /**
     * Test of ComputerService.getCount().
     */
    @Test
    public final void testGetCount() {
        mCompanyService.getCount();
    }

    /**
     * Test of ComputerService.add(Computer computer).
     */
    @Test
    public final void testAddNull() {
        //add a computer with NULL fields.
        final String compName = "Xiaomi";
        final Company company = new Company(compName);
        mCompanyService.add(company);
        mCompanyService.delete(company);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testAddTwice() {
        //add the same computer twice
        final String compName = "Xiaomi";
        final Company company = new Company(compName);
        try {
            mCompanyService.add(company);
            mCompanyService.add(company);
        } catch (final IllegalArgumentException e) {
            mCompanyService.delete(company);
            throw e;
        }
    }

    @Test
    public final void testAddWrong() {
        //add a computer with NULL fields.
        final String compName = "Unknown company";
        final Company wrongCompany = new Company(-1, compName);
        boolean passed = false;

        //try an invalid id
        try {
            mCompanyService.add(wrongCompany);
            passed = true;
        } catch (final IllegalArgumentException e) {
        }
        assertFalse(passed);

    }

    @Test(expected = NoSuchElementException.class)
    public final void testDelete() {
        final String companyName = "Xiaomi";
        final int count = mComputerService.listLikeName(0, 0, companyName).size();

        final Company company = new Company(companyName);
        mCompanyService.add(company);
        mCompanyService.delete(company);

        assertTrue(mCompanyService.listLikeName(0, 0, companyName).size() == count);

        mCompanyService.delete(company);
    }

    /**
     * test Company retrieval.
     */
    @Test
    public final void testRetrieve() {
        final Company company = mCompanyService.retrieve(1L);
        assertTrue(company.getName() != null && !company.getName().trim().isEmpty());
        assertTrue(company.getId().equals(1L));
    }

    /**
     * test invalid company retrieval.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testRetrieveWrong() {
        mCompanyService.retrieve(-1L);

    }

    /**
     * test non-existing computer retrieval.
     */
    @Test(expected = NoSuchElementException.class)
    public final void testRetrieveNonExistant() {
        final long tooBigId = 9999999L;
        mCompanyService.retrieve(tooBigId);
    }

    @Test
    public final void testSearchById() {
        final long tooBigId = 9999999L;

        assertTrue(mCompanyService.search(1L) != null);
        assertTrue(mCompanyService.search(tooBigId) == null);
    }
}
