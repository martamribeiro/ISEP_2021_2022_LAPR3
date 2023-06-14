package auth.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    public void checkUserRoleWithBlankid(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()-> new UserRole("", "descriptionm"));
        assertEquals(thrown.getMessage(), "UserRole id and/or description cannot be blank.");
    }

    @Test
    public void checkUserRoleWithBlankDescription(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()-> new UserRole("id", ""));
        assertEquals(thrown.getMessage(), "UserRole id and/or description cannot be blank.");
    }

    @Test
    public void checkGetId(){
        UserRole role = new UserRole("id", "desc");
        assertEquals(role.getId(), "id".trim().toUpperCase());
    }

    @Test
    public void checkGetDescription(){
        UserRole role = new UserRole("id", "desc");
        assertEquals(role.getDescription(), "desc");
    }

    @Test
    public void checkHasId(){
        UserRole role = new UserRole("id", "desc");
        assertTrue(role.hasId("id"));
        assertFalse(role.hasId("not"));
        assertFalse(role.hasId(""));
    }

    @Test
    public void checkEquals(){
        UserRole role = new UserRole("id", "desc");
        UserRole otherRole = new UserRole("id", "desc");
        UserRole otherRoleDiff = new UserRole("idddd", "desc");
        String notUserRole = "notUserrole";

        assertEquals(role, role); //same obj
        assertEquals(role, otherRole); //same attribute of comparison
        assertNotEquals(role, notUserRole); //diff class
        assertNotEquals(role, null); //null
        assertNotEquals(role, otherRoleDiff); //null

    }

}