package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.dao.ICompanyDao;
import com.excilys.cdb.dao.IComputerDao;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;


/**
 *
 * @author Nicolas THIERION.
 * @version 0.1.0
 */
public class ServiceImpl implements Service {

    /* ***
     * ATTRIBUTES
     */
    private final IComputerDao mComputerDao;
    private ICompanyDao        mCompanyDao;

    public ServiceImpl(IComputerDao dao) {
        mComputerDao = dao;
    }

    @Override
    public List<Computer> listComputersByName(int begin, int count) {
        return mComputerDao.listByName(begin, count);
    }

    @Override
    public List<Company> listCompaniesByName(int begin, int nb) {
        return mCompanyDao.listByName(begin, nb);
    }

    @Override
    public void addComputer(Computer computer) {
        mComputerDao.add(computer);
    }

    @Override
    public void updateComputer(Computer computer) {
        mComputerDao.update(computer);
    }

    @Override
    public void deleteComputer(Computer computer) {
        mComputerDao.delete(computer);
    }

}
