package com.excilys.cdb.persistence.dao.mysql;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.EntityField;
import com.excilys.cdb.model.User;
import com.excilys.cdb.model.UserAuthoritiesAssoc;
import com.excilys.cdb.persistence.dao.DaoException;
import com.excilys.cdb.persistence.dao.IUserDao;


//TODO doc
@Repository("userDao")
public class UserDao extends AbstractMySqlDao<User> implements IUserDao {

    static final String REQ_SELECT_USERS_FILENAME  = "select_users.hql";
    static final String REQ_COUNT_USERS_FILENAME   = "select_user_count.hql";

    public UserDao() {
        super(REQ_SELECT_USERS_FILENAME, REQ_COUNT_USERS_FILENAME);
    }

    @Override
    public User add(User user) {
        mAssertUserValid(user);
        daoSave(user);

        for (final User.Authority role : user.getAuthorities()) {
            getCurrentSession().save(new UserAuthoritiesAssoc(user, role));
        }

        return user;
    }


    @Override
    public void delete(String userName) {
        final List<User> userList = listEqual(UserField.NAME, userName);
        if (userList.isEmpty()) {
            return;
        }
        final User user = userList.get(0);

        // delete authorities from this user first.
        for (final User.Authority role : user.getAuthorities()) {
            getCurrentSession().delete(new UserAuthoritiesAssoc(user, role));
        }
        daoDelete(user);
    }

    @Override
    public List<User> listEqual(EntityField<User> field, String value, int offset, int count) {

        // check offset parameter
        if (offset < 0) {
            throw new IllegalArgumentException("search offset cannot be negative");
        }

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        count = (count < 0 ? Integer.MAX_VALUE : count);

        // check field validity
        mCheckField(field);

        // get HQL query string & format it
        String hqlStr = getQuery(REQ_SELECT_USERS_FILENAME);
        // place field criteria & order by hand...
        hqlStr = String.format(hqlStr, field.getLabel(), field.getLabel());
        final Query query = createQuery(hqlStr);

        query.setString("value", value);

        // set range parameters
        query.setFirstResult(offset).setMaxResults(count);

        // execute the query
        @SuppressWarnings("unchecked")
        final List<User> resList = query.list();
        return resList;
    }

    @Override
    public int getCountEqual(EntityField<User> field, String value) throws IllegalArgumentException, DaoException {

        // check name parameter
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }

        // check field validity
        mCheckField(field);

        // get HQL query string & format it
        String hqlStr = getQuery(REQ_COUNT_USERS_FILENAME);

        // place field criteria & order by hand...
        hqlStr = String.format(hqlStr, field.getLabel());
        final Query query = createQuery(hqlStr);
        query.setString("value", value);

        // execute the query
        final Long count = (Long) query.uniqueResult();
        return count.intValue();
    }

    /* ***
     * PRIVATE METHODS
     */

    private void mCheckField(EntityField<User> field) {
        if (!(field instanceof UserField)) {
            throw new IllegalArgumentException("Field must be of type " + UserField.class.getName());
        }
    }

    private void mAssertUserValid(User user) {
        if (user.getUserame() == null || user.getUserame().trim().isEmpty()) {
            throw new IllegalArgumentException("username must not be empty.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("password must not be empty.");
        }
    }

}
