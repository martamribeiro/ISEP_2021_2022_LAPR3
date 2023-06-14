package auth.domain.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import java.util.Objects;

/**
 *
 * @author João Wolff <1200049@isep.ipp.pt> inspired in class by: Paulo Maio <pam@isep.ipp.pt>
 */
public class User {

    private final Email id;
    private final Password password;
    private final String name;
    private UserRole role = null;

    public User(Email id, Password pwd, String name)
    {
        if ( (!ObjectUtils.allNotNull(id, pwd)) || StringUtils.isBlank(name))
            throw new IllegalArgumentException("User cannot have an id, password or name as null/blank.");

        this.id = id;
        this.password = pwd;
        this.name = name.trim();
    }

    public Email getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean hasId(Email id)
    {
        return this.id.equals(id);
    }

    public boolean hasPassword(String pwd)
    {
        return this.password.checkPassword(pwd);
    }

    public void addRole(UserRole role)
    {
        this.role = role;
    }

    public boolean removeRole(UserRole role)
    {
        if(role == null){
            return false;
        }
        this.role = null;
        return true;
    }

    public boolean hasRole(UserRole role)
    {
        return this.role.equals(role);
    }

    public boolean hasRole(String roleId)
    {
        return Objects.equals(this.role.getId(), roleId);
    }

    public UserRole getRole()
    {
        return this.role;
    }


    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 23 * hash + this.id.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        // Inspired in https://www.sitepoint.com/implement-javas-equals-method-correctly/

        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        // field comparison
        User obj = (User) o;
        return Objects.equals(this.id, obj.id);
    }

    @Override
    public String toString()
    {
        return String.format("%s - %s", this.id.toString(), this.name);
    }
}
