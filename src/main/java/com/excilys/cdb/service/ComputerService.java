package com.excilys.cdb.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.IComputerDao;

public class ComputerService implements IComputerService {

    /* ***
     * ATTRIBUTES
     */
    /** Computer DAO used by this Service. */
    private IComputerDao mComputerDao;

    /* ***
     * CONSTRUCTORS
     */
    /**
     * Argument constructor. Creates a new Service, given the computer DAO
     * implementation.
     *
     * @param computerDao
     *            Computer DAO to be used by this service.
     */
    public ComputerService(IComputerDao computerDao) {
        mComputerDao = computerDao;
    }


    /* ***
     * ACCESSORS
     */
    /**
     * Sets the {@code IComputerDao} to be used by this Service.
     *
     * @param computerDao
     *            Computer Dao to use.
     */
    public void setComputerDao(IComputerDao computerDao) {
        mComputerDao = computerDao;
    }

    /**
     * Tells if this instance is properly initialized (ie : Correct DAO has been
     * given).
     *
     * @return true if initialized.
     */
    public boolean isInitialized() {
        return mComputerDao != null;
    }

    /* ***
     * SERVICE METHODS
     */

    @Override
    public List<Computer> listByName(int offset, int count) {
        return mComputerDao.listByName(offset, count);
    }

    @Override
    public List<Computer> listLikeName(int offset, int count, String name) {

        return mComputerDao.listLikeName(offset, count, name);

    }

    @Override
    public Computer add(Computer computer) throws IllegalArgumentException {
        try {
            return mComputerDao.add(computer);
        } catch (final DaoException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void update(Computer computer) throws NoSuchElementException {
        try {
            mComputerDao.update(computer);
        } catch (final DaoException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws NoSuchElementException {
        try {
            mComputerDao.delete(id);
        } catch (final DaoException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return mComputerDao.getCount();
    }

    @Override
    public int getCount(String queryString) {
        return mComputerDao.getCount(queryString);
    }


    @Override
    public Computer retrieve(long computerId) throws NoSuchElementException, IllegalArgumentException {
        final Computer computer;
        computer = search(computerId);
        if (computer == null) {
            throw new NoSuchElementException("no computer with id = " + computerId + " found.");
        }

        return computer;
    }

    @Override
    public Computer search(long computerId) throws IllegalArgumentException {
        return mComputerDao.searchById(computerId);
    }
}
