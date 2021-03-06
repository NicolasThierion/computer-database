package com.excilys.cdb.tests.dto;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.IComputerDao.ComputerField;
import com.excilys.cdb.persistence.dao.mysql.ComputerDao;


/**
 * Unit test for ComputerDto methods.
 *
 * @author Nicolas THIERION.
 * @version 0.2.0
 *
 */
public class ComputerDtoTest {

    /** Logger used in these tests. */
    private Logger mLogger;

    @Autowired
    private SessionFactory mSessionFactory;

    @Before
    public final void initLogger() {
        mLogger = LoggerFactory.getLogger(ComputerDtoTest.class);
    }

    /**
     * Test of ComputerDto constructors.
     */
    @SuppressWarnings("deprecation")
    @Test
    public final void testNewComputerDto() {
        final String name = "Surface pro 4";
        final Company company = new Company();
        final LocalDate releaseDate = java.time.LocalDate.of(1999, 1, 1);
        final LocalDate discontinuedDate = java.time.LocalDate.of(2010, 1, 1);

        // test default constructor
        ComputerDto dto = new ComputerDto();
        assertTrue(dto.equals(new ComputerDto(dto)));
        mLogger.debug("created object " + dto.toString());

        //test empty computer
        final Computer nullComputer = new Computer();
        dto = ComputerDto.fromComputer(nullComputer);
        assertTrue(nullComputer.getId() == dto.getId());
        assertTrue(dto.equals(new ComputerDto(dto)));
        mLogger.debug("created object " + dto.toString());

        //test computer with parameters
        Computer computer;
        final Long bigId = 9999L;
        for (final Long id : new Long[] {null, 1L, bigId}) {
            for (final String iName : new String[] {null, name}) {
                for (final Company iCompany : new Company[] {null, company}) {
                    for (final LocalDate rd : new LocalDate[] {null, releaseDate}) {
                        for (final LocalDate dd : new LocalDate[] {null, discontinuedDate}) {

                            computer = new Computer();
                            if (id != null) {
                                computer.setId(id);
                            }
                            if (iName != null) {
                                computer.setName(iName);
                            }
                            computer.setIntroduced(rd);
                            computer.setDiscontinued(dd);
                            computer.setCompany(iCompany);

                            dto = ComputerDto.fromComputer(computer);
                            assertTrue(dto.toComputer().equals(computer));
                            assertTrue(dto.isValid() == computer.isValid());
                        }
                    }
                }
            }
        }
    }

    @Test
    @Ignore("should mock Dao")
    public final void testDtoFromList() {
        final List<Computer> computers = new ComputerDao(mSessionFactory).listBy(ComputerField.NAME);
        final List<ComputerDto> dtos = ComputerDto.fromComputers(computers);
        assertTrue(computers.size() == dtos.size());
        final Iterator<ComputerDto> dtoIt = dtos.iterator();
        final Iterator<Computer> computerIt = computers.iterator();

        while (dtoIt.hasNext()) {
            assertTrue(dtoIt.next().toComputer().equals(computerIt.next()));
        }
    }
}
