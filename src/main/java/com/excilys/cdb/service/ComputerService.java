package com.excilys.cdb.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.IComputerDao;
import com.excilys.cdb.persistence.mapper.ComputerMapper;

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
        mCheckParams(computerDao);
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
        return mComputerDao.listBy(ComputerMapper.Field.NAME, offset, count);
    }

    @Override
    public List<Computer> listLikeName(int offset, int count, String name) {
        return mComputerDao.listLike(ComputerMapper.Field.NAME, name, offset, count);
    }

    @Override
    public Computer add(Computer computer) throws IllegalArgumentException {
        return mComputerDao.add(computer);
    }

    @Override
    public void update(Computer computer) throws NoSuchElementException {
        mComputerDao.update(computer);
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
    public int getCount(String name) {
        return mComputerDao.getCountLike(ComputerMapper.Field.NAME, name);
    }

    @Override
    public Computer search(long computerId) throws IllegalArgumentException {
        if (computerId < 0) {
            throw new IllegalArgumentException("Computer id must be positive");
        }
        final List<Computer> list =  mComputerDao.listEqual(ComputerMapper.Field.ID, "" + computerId);
        return (list.isEmpty() ? null : list.get(0));
    }

    private void mCheckParams(IComputerDao computerDao) {
        if (computerDao == null) {
            throw new NullPointerException("connection cannot be null");
        }
    }
}
