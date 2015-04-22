package com.excilys.cdb.tests.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.dao.ICompanyDao;
import com.excilys.cdb.persistence.dao.mysql.CompanyDao;
import com.excilys.cdb.persistence.mapper.CompanyMapper;

/**
 * Unit test for ComputerDao methods.
 *
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
public class CompanyDaoTest {

    /** Company Dao used for this tests. */
    private ICompanyDao  mCompanyDao;

    /**
     * init Company Dao.
     */
    @Before
    public final void init() {
        mCompanyDao = CompanyDao.getInstance();
    }

    /**
     * Test of CompanyDao.listBy().
     */
    @Test
    public final void testListByName() {

        List<Company> list;
        //test list construction of various size
        for (final int count : new int[] {2, mCompanyDao.getCount(), 0}) {
            list = mCompanyDao.listBy(CompanyMapper.Field.NAME, 0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mCompanyDao.listBy(CompanyMapper.Field.NAME, mCompanyDao.getCount(), mCompanyDao.getCount());
        assertTrue(list.size() == 0);

        final int count = mCompanyDao.getCount();
        list = mCompanyDao.listBy(CompanyMapper.Field.NAME, 0, count + 2);
        assertTrue(list.size() == count);

        // negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mCompanyDao.listBy(CompanyMapper.Field.NAME, invalidOffset, count + 2);
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
        final List<Company> list = mCompanyDao.listLike(CompanyMapper.Field.NAME, name);
        assertTrue(list.size() == 1);
    }

    /**
     * Test of CompanyDao.listBy().
     */
    @Test
    public final void testListEqualId() {

        List<Company> list = mCompanyDao.listEqual(CompanyMapper.Field.ID, "1");
        assertTrue(list.size() == 1);
        list = mCompanyDao.listEqual(CompanyMapper.Field.ID, "" + Long.MAX_VALUE);
        assertTrue(list.size() == 0);
        list = mCompanyDao.listLike(CompanyMapper.Field.ID, "1");
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
