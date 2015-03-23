package com.excilys.cdb.tests;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Unit test for Computer methods.
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
public class ComputerTest {

    /** Logger used in these tests. */
    private Logger mLogger;

    @Before
    public final void initLogger() {
        mLogger = LoggerFactory.getLogger(ComputerTest.class);
    }

    /**
     * Test of Computer constructors.
     */
    @Test
    public final void newComputer() {
        final String name = "Surface pro 4";
        final Company company = new Company();
        final LocalDate releaseDate = java.time.LocalDate.of(1999, 1, 1);
        final LocalDate discontinuedDate = java.time.LocalDate.of(2010, 1, 1);

        //test empty computer
        final Computer nullComputer = new Computer();
        Computer copyConputer = new Computer(nullComputer);
        assertTrue(copyConputer.equals(nullComputer));
        mLogger.info("created object " + nullComputer.toString());

        //test computer with parameters
        Computer computer;
        for (final LocalDate rd : new LocalDate[] {null, releaseDate}) {
            for (final LocalDate dd : new LocalDate[] {null, discontinuedDate}) {

                computer = new Computer(name, company, rd, dd);
                copyConputer = new Computer(computer);
                assertTrue(copyConputer.equals(computer));
            }
        }

    }

    /**
     * Test of Computer constructor with null name.
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public final void newNullComputer() {
        final String name = null;
        final Computer computer = new Computer(name);
    }

    /**
     * Test of Computer constructor with invalid dates.
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public final void newInvalidDatesComputer() {
        final String name = "Surface pro 4";
        final Company company = new Company();
        final LocalDate releaseDate = java.time.LocalDate.of(2010, 1, 1);
        final LocalDate discontinuedDate = java.time.LocalDate.of(1999, 1, 1);

        final Computer computer = new Computer(name, company, releaseDate, discontinuedDate);
    }
}
