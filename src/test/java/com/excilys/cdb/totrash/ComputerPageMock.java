package com.excilys.cdb.totrash;

import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

/**
 *
 * @author Nicolas THIERION
 *
 *         TODO JavaDoc.
 * @param <T>
 */
public class ComputerPageMock extends Page<Computer> implements Serializable {
    /**
     *
     */
    private static final long   serialVersionUID   = -6040090642271321434L;
    private static final int TOTAL_RESULT_CONUT = 100;
    private static final String COMPUTER_BASENAME  = "Computer";
    private static final String COMPANY_BASENAME   = "Company";


    public ComputerPageMock() {


        final LinkedList<Computer> computers = new LinkedList<Computer>();

        for (int i = 0; i < TOTAL_RESULT_CONUT; ++i) {

            final String compunerName = COMPUTER_BASENAME + " " + i;
            final Company company = new Company(COMPANY_BASENAME + " " + i);
            final Computer computer = new Computer(compunerName, company, LocalDate.now().minusYears(i + 1), LocalDate
                    .now().minusYears(i));
            computers.add(computer);
        }

        when(this.getContent()).thenReturn(computers);
        when(this.getCount()).thenReturn(computers.size());
        when(this.getPageNum()).thenReturn(2);
        when(this.getOffset()).thenReturn(this.getCount() * (this.getPageNum() - 1));
        when(this.getTotalCount()).thenReturn(TOTAL_RESULT_CONUT);
    }

    @Override
    public void setContent(List<Computer> content) {

    }

    @Override
    public void setLength(int length) {

    }

    @Override
    public void setOffset(int offset) {

    }

    @Override
    public void setPageNum(int pageNum) {

    }

    @Override
    public void setTotalCount(int total) {

    }

}
