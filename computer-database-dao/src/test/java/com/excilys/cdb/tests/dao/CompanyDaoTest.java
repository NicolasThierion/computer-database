package com.excilys.cdb.tests.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.ICompanyDao;
import com.excilys.cdb.persistence.dao.ICompanyDao.CompanyField;

/**
 * Unit test for ComputerDao methods.
 *
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class CompanyDaoTest {

    /** Company Dao used for this tests. */
    @Autowired
    private ICompanyDao  mCompanyDao;

    /**
     * Test of CompanyDao.listBy().
     */
    @Test
    public final void testListByName() {

        List<Company> list;
        //test list construction of various size
        for (final int count : new int[] {2, mCompanyDao.getCount()}) {
            list = mCompanyDao.listBy(CompanyField.NAME, 0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mCompanyDao.listBy(CompanyField.NAME, mCompanyDao.getCount(), mCompanyDao.getCount());
        assertTrue(list.size() == 0);

        final int count = mCompanyDao.getCount();
        list = mCompanyDao.listBy(CompanyField.NAME, 0, count + 2);
        assertTrue(list.size() == count);

        // negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mCompanyDao.listBy(CompanyField.NAME, invalidOffset, count + 2);
        } catch (final IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    /**
     * Test of CompanyDao.listBy().
     */
    @Test
    public final void testListLikeName() {

        final String name = "Apple";
        final List<Company> list = mCompanyDao.listLike(CompanyField.NAME, name);
        assertTrue(list.size() == 1);
    }

    /**
     * Test of CompanyDao.listBy().
     */
    @Test
    public final void testListEqualId() {

        List<Company> list = mCompanyDao.listEqual(CompanyField.ID, "1");
        assertTrue(list.size() == 1);
        list = mCompanyDao.listEqual(CompanyField.ID, "" + Long.MAX_VALUE);
        assertTrue(list.size() == 0);
        list = mCompanyDao.listLike(CompanyField.ID, "1");
        assertTrue(list.size() > 1);
    }

    /**
     * Test of CompanyDao.getCount().
     */
    @Test
    public final void testGetCount() {
        mCompanyDao.getCount();
    }

}
