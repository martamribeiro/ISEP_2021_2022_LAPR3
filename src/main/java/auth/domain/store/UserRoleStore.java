package auth.domain.store;

import auth.domain.model.UserRole;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author João Wolff <1200049@isep.ipp.pt> inspired in class by: Paulo Maio <pam@isep.ipp.pt>
 */
public class UserRoleStore {

    private final Set<UserRole> store = new HashSet<UserRole>();

    public UserRole create(String id, String description)
    {
        return new UserRole(id,description);
    }

    public boolean add(UserRole role)
    {
        if (role != null) {
            if (!exists(role))
                return this.store.add(role);
        }
        return false;
    }

    public boolean remove(UserRole role)
    {
        if (role != null)
            return this.store.remove(role);
        return false;
    }

    public Optional<UserRole> getById(String id)
    {
        for(UserRole role: this.store)
        {
            if(role.hasId(id))
                return Optional.of(role);
        }
        return Optional.empty();
    }

    public boolean exists(String id)
    {
        Optional<UserRole> result = getById(id);
        return result.isPresent();
    }

    public boolean exists(UserRole role)
    {
        return this.store.contains(role);
    }
}
