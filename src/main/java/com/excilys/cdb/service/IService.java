package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 *
 * @author Nicolas THIERION
 * @version 0.1.0
 *
 * TODO : First shot of service. Must probably be re-designed.
 */
public interface IService {

    List<Computer> listComputersByName(int offset, int count);

    List<Computer> listComputersLikeName(int offset, int count, String name);

    List<Company> listCompaniesByName(int begin, int nb);

    void addComputer(Computer computer);

    void updateComputer(Computer computer);

    void deleteComputer(Computer computer);

    int getComputerCount();


}
