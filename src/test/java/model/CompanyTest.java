package model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;

/**
 * Unit test for Company methods.
 *
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
public class CompanyTest {

    /** Logger used in these tests. */
    private Logger mLogger;

    @Before
    public final void initLogger() {
        mLogger = LoggerFactory.getLogger(CompanyTest.class);
    }

    /**
     * Test of Computer constructors.
     */
    @Test
    public final void testNewCompany() {
        final String name = "Xiaomi";
        final Company nullCompany = new Company();
        Company copyCompany = new Company(nullCompany);
        assertTrue(copyCompany.equals(nullCompany));
        mLogger.info("Created company " + nullCompany);

        final Company company = new Company(name);
        copyCompany = new Company(company);
        assertTrue(copyCompany.equals(company));
        mLogger.info("Created company " + copyCompany);

    }

    /**
     * Test of Company constructor with null name.
     */
    @Ignore("null companies allowed at this moment")
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public final void testNewNullCompany() {
        final String name = null;
        final Company nullCompany = new Company(name);
    }
}
