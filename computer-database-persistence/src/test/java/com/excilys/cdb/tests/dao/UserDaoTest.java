package com.excilys.cdb.tests.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.model.User;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.IUserDao;
import com.excilys.cdb.persistence.dao.IUserDao.UserField;

/**
 * Unit test for UserDao methods.
 *
 * @author Nicolas THIERION.
 * @version 0.3.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class UserDaoTest {

    /** User Dao used for this tests. */
    @Autowired
    private IUserDao mUserDao;

    private User         mUniqueUser;
    private String       mUniqueName;
    private String       mPassword;

    private final String mNonExistentName = "nobody";


    public User createUniqueUser() {
        // add one user
        mUniqueName = "user" + java.time.Clock.systemUTC().millis();
        mPassword = "password" + java.time.Clock.systemUTC().millis();

        mUniqueUser = new User(mUniqueName, mPassword);
        mUserDao.add(mUniqueUser);
        return mUniqueUser;
    }

    public void deleteUniqueUser() {
        mUserDao.delete(mUniqueUser);
    }

    /**
     * Test of UserDao.listLike().
     */
    @Test
    public final void testListByName() {

        List<User> list;
        //test list construction of various size
        for (final int count : new int[] {2, mUserDao.getCount()}) {
            list = mUserDao.listBy(UserField.NAME, 0, count);
            assertTrue(list.size() == count);
        }

        //test list construction out of bounds
        list = mUserDao.listBy(UserField.NAME, mUserDao.getCount(), mUserDao.getCount());
        assertTrue(list.size() == 0);

        final int count = mUserDao.getCount();
        list = mUserDao.listBy(UserField.NAME, 0, count + 2);
        assertTrue(list.size() == count);

        //negative offset
        boolean passed = true;
        try {
            final int invalidOffset = -1;
            list = mUserDao.listBy(UserField.NAME, invalidOffset, count + 2);
        } catch (final IllegalArgumentException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    /**
     * Test of UserDao.listLike().
     */
    @Test
    public final void testListLikeName() {
        createUniqueUser();

        List<User> list = mUserDao.listLike(UserField.NAME, mUniqueName);
        assertTrue(list.size() == 1);

        list = mUserDao.listLike(UserField.NAME, mNonExistentName);
        assertTrue(list.size() == 0);

        deleteUniqueUser();
    }

    /**
     * Test of UserDao.listLike().
     */
    @Test
    public final void testListEqualId() {
        createUniqueUser();

        List<User> list = mUserDao.listEqual(UserField.ID, mUniqueName);
        assertTrue(list.size() == 1);
        list = mUserDao.listEqual(UserField.ID, "" + mNonExistentName);
        assertTrue(list.size() == 0);
        list = mUserDao.listBy(UserField.ID);
        assertTrue(list.size() > 1);

        deleteUniqueUser();
    }

    /**
     * Test of UserDao.getCount().
     */
    @Test
    public final void testGetCount() {
        mUserDao.getCount();
    }


    public final void testAddTwice() {
        boolean passed = true;
        try {
            // add the same user twice
            createUniqueUser();
            mUserDao.add(mUniqueUser);
        } catch (final IllegalArgumentException e) {
            passed = false;
        }
        deleteUniqueUser();
        assertTrue(!passed);
    }

    @Test
    public final void testAddWrong() {
        // add a user with NULL fields.
        boolean passed = true;

        //try an invalid id
        try {
            mUserDao.add(new User(null, null));
        } catch (IllegalArgumentException | DaoException e) {
            passed = false;
        }
        assertFalse(passed);
    }

    @Test
    @Ignore("not implemented")
    public final void testUpdate() {

        final String userName = "user" + java.time.Clock.systemUTC().millis();
        final String password = "password" + java.time.Clock.systemUTC().millis();

        // add a new user
        createUniqueUser();

        List<User> list = mUserDao.listLike(UserField.NAME, mUniqueName);
        final int count = mUserDao.listLike(UserField.NAME, userName).size();


        // update its properties.
        mUniqueUser.setUserName(userName);
        mUniqueUser.setPassword(password);
        mUserDao.update(mUniqueUser);

        // ensure modification are effective in db.
        list = mUserDao.listLike(UserField.NAME, mUniqueName);
        assertTrue(list.size() == 0);

        list = mUserDao.listLike(UserField.NAME, userName);
        assertTrue(list.size() == count + 1);

        // ensure user has been updated correctly
        final User userCopy = mUserDao.listEqual(UserField.ID, mUniqueUser.getId().toString()).get(0);
        assertTrue(mUniqueUser.equals(userCopy));

        deleteUniqueUser();

        boolean passed = false;

        // try updating non-existing user.
        try {
            mUserDao.update(mUniqueUser);
            passed = true;
        } catch (final NoSuchElementException | DaoException e) {
        }
        assertFalse(passed);

        // try updating invalid user.
        try {
            mUserDao.update(new User());
            passed = true;
        } catch (final IllegalArgumentException e) {
        }

        assertFalse(passed);
    }
}
