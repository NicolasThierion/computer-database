package com.excilys.cdb.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.IComputerDao;
import com.excilys.cdb.persistence.mapper.ComputerMapper;

/**
 * Spring-autowired Computer service. Offers CRUD services for computers.
 *
 * @author Nicolas THIERION.
 *
 */
@Service
public class ComputerService implements IComputerService {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerService.class);

    /* ***
     * ATTRIBUTES
     */
    /** Computer DAO used by this Service. */
    @Autowired
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

    /**
     * Default constructor. Will not be usable until ComputerService has been
     * set.
     *
     */
    public ComputerService() {
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
        LOG.info("listByName(" + offset + ", " + count + ")");
        return mComputerDao.listBy(ComputerMapper.Field.NAME, offset, count);
    }

    @Override
    public List<Computer> listLikeName(int offset, int count, String name) {
        LOG.info("listByName(" + offset + ", " + count + ", " + name + ")");
        return mComputerDao.listLike(ComputerMapper.Field.NAME, name, offset, count);
    }

    @Override
    public Computer add(Computer computer) throws IllegalArgumentException {
        LOG.info("add(" + computer + ")");
        return mComputerDao.add(computer);
    }

    @Override
    public void update(Computer computer) throws NoSuchElementException {
        LOG.info("update(" + computer + ")");
        mComputerDao.update(computer);
    }

    @Override
    public void delete(long... ids) throws NoSuchElementException {
        LOG.info("delete(" + ids + ")");

        // TODO use SQL to delete many
        for (final long id : ids) {
            try {
                mComputerDao.delete(id);
            } catch (final DaoException e) {
                throw new NoSuchElementException(e.getMessage());
            }
        }
    }

    @Override
    public int getCount() {
        LOG.info("getCount()");
        return mComputerDao.getCount();
    }

    @Override
    public int getCount(String name) {
        LOG.info("getCount(" + name + ")");
        return mComputerDao.getCountLike(ComputerMapper.Field.NAME, name);
    }

    @Override
    public Computer search(long computerId) throws IllegalArgumentException {
        LOG.info("search(" + computerId + ")");
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
