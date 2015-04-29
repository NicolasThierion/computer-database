package com.excilys.cdb.persistence.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.EntityField;
import com.excilys.cdb.model.User;

@Transactional
public interface IUserDao extends ICrudDao<String, User> {

    public enum UserField implements EntityField<User> {
        NAME("user.username"), ID(NAME.getLabel()), PASSWORD("user.password");

        String mLabel;

        UserField(String label) {
            mLabel = label;
        }

        @Override
        public String toString() {
            return mLabel;
        }

        @Override
        public String getLabel() {
            return mLabel;
        }
    }

    @Override
    public User add(User user);

    @Override
    List<User> listEqual(EntityField<User> field, String value, int offset, int count);


    @Override
    public void delete(String userName);

    @Override
    default int getCount() {
        return getCountLike(UserField.ID, "");
    }

    @Override
    public int getCountEqual(EntityField<User> field, String value) throws IllegalArgumentException, DaoException;
}
