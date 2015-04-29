package com.excilys.cdb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

//TODO doc
@Entity()
@Table(name = "users")
public class User implements Identifiable<String>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6452644139674978333L;

    @Id
    @Column(name = "username")
    @NotNull
    @NotBlank
    private String            username;

    @Column(name = "password")
    private String            password;

    private Authority         authority;

    public enum Authority {
        USER("user"), ADMIN("admin");

        private String mRole;

        Authority(String role) {
            mRole = role;
        }

        @Override
        public String toString() {
            return mRole;
        }
    };

    private void mNewUser(String userName, String password, Authority authority) {

        this.username = userName;
        this.password = password;
        this.authority = authority;

    }

    public User() {
        mNewUser(null, null, null);
    }

    public User(String userName, String password) {
        mNewUser(userName, password, null);
    }

    public User(String userName, String password, Authority authority) {
        mNewUser(userName, password, authority);
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getId() {
        return username;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authority == null) ? 0 : authority.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (authority != other.authority) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }




}
