package com.excilys.cdb.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

//TODO doc
//TODO DTO
@Entity()
@Table(name = "users")
public class User implements Identifiable<String>, Serializable {

    public enum Authority {
        USER("user"), ADMIN("admin"), NONE("none");

        private String authority;

        Authority(String role) {
            authority = role;
        }

        @Override
        public String toString() {
            return authority;
        }
    };

    /**
     *
     */
    private static final long serialVersionUID = -6452644139674978333L;
    private static final Authority DEFAULT_ROLE     = Authority.NONE;

    @Id
    @Column(name = "username")
    @NotNull
    private String            username;

    @Column(name = "password")
    private String            password;

    // need a dedicated assoc table for that... @see UserAuthoritiesAssoc
    @Transient
    private Set<Authority>         authorities;

    @Column(name = "enabled")
    private boolean                enabled          = true;


    private void mNewUser(String userName, String password, Authority... authorities) {

        this.username = userName;
        this.password = password;

        this.authorities = new HashSet<Authority>();
        setAuthorities(authorities);
    }

    public User() {
        mNewUser(null, null, Authority.NONE);
    }

    public User(String userName, String password) {
        mNewUser(userName, password, DEFAULT_ROLE);
    }

    public User(String userName, String password, Authority authority) {
        mNewUser(userName, password, authority);
    }

    public String getUserame() {
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authority... authorities) {
        this.authorities.addAll(Arrays.asList(authorities));
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
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
        if (authorities != other.authorities) {
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
