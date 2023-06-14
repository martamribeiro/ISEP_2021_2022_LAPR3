package auth;

import auth.domain.model.Email;
import auth.domain.model.User;
import auth.mappers.UserRoleMapper;
import auth.mappers.dto.UserRoleDTO;

/**
 *
 * @author João Wolff <1200049@isep.ipp.pt> inspired in class by: Paulo Maio <pam@isep.ipp.pt>
 */
public class UserSession {

    private User user = null;

    public UserSession()
    {
        this.user = null;
    }

    public UserSession(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("Argument cannot be null.");
        this.user = user;
    }

    public void doLogout()
    {
        this.user = null;
    }

    public boolean isLoggedIn()
    {
        return this.user != null;
    }

    public boolean isLoggedInWithRole(String roleId)
    {
        if (isLoggedIn())
        {
            return this.user.hasRole(roleId);
        }
        return false;
    }

    public String getUserName()
    {
        if (isLoggedIn())
            this.user.getName();
        return null;
    }

    public Email getUserId()
    {
        if (isLoggedIn())
            return this.user.getId();
        return null;
    }

    public UserRoleDTO getUserRole()
    {
        if (isLoggedIn()) {
            UserRoleMapper mapper = new UserRoleMapper();
            return mapper.toDTO(this.user.getRole());
        }
        return null;
    }



}
