package auth.mappers;

import auth.domain.model.UserRole;
import auth.mappers.dto.UserRoleDTO;

public class UserRoleMapper {

    public UserRoleDTO toDTO(UserRole role)
    {
        return new UserRoleDTO(role.getId(),role.getDescription());
    }

  /*  public List<UserRoleDTO> toDTO(List<UserRole> roles)
    {
        List<UserRoleDTO> rolesDTO = new ArrayList<>();
        for(UserRole role:roles)
        {
            rolesDTO.add(this.toDTO(role));
        }
        return rolesDTO;
    }*/
}
