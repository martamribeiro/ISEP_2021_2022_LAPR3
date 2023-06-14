package auth.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void checkIdNotNull(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()-> new User(null, new Password("123345"), "name"));
        assertEquals("User cannot have an id, password or name as null/blank.", thrown.getMessage());
    }

    @Test
    public void checkPwsNotNull(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()-> new User(new Email("email@email.com"), null, "name"));
        assertEquals("User cannot have an id, password or name as null/blank.", thrown.getMessage());
    }

    @Test
    public void checkNameBlank(){
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, ()-> new User(new Email("email@email.com"), new Password("123345"), ""));
        assertEquals("User cannot have an id, password or name as null/blank.", thrown.getMessage());
    }

    @Test
    public void testGetName(){
        User newUser = new User(new Email("email@email.com"), new Password("123345"), "Joao");
        assertEquals("Joao", newUser.getName());
    }

    @Test
    public void testRemoveRoleNull(){
        User newUser = new User(new Email("email@email.com"), new Password("123345"), "Joao");
        newUser.addRole(new UserRole("Traffic manager", "Traffic manager"));
        assertFalse(newUser.removeRole(null));
    }

    @Test
    public void testRemoveRoleExistent(){
        User newUser = new User(new Email("email@email.com"), new Password("123345"), "Joao");
        newUser.addRole(new UserRole("Traffic manager", "Traffic manager"));
        assertTrue(newUser.removeRole(newUser.getRole()));
    }

    @Test
    public void testUserHasRole(){
        User newUser = new User(new Email("email@email.com"), new Password("123345"), "Joao");
        newUser.addRole(new UserRole("T1", "Traffic manager"));
        assertTrue(newUser.hasRole("T1"));
    }

    @Test
    public void testUserHasRoleObj(){
        User newUser = new User(new Email("email@email.com"), new Password("123345"), "Joao");
        UserRole role = new UserRole("T1", "Traffic manager");
        newUser.addRole(role);
        assertTrue(newUser.hasRole(role));
    }

    @Test
    public void testEqualsMethod(){
        User newUser = new User(new Email("email@email.com"), new Password("123345"), "Joao");
        User newUser2 = new User(new Email("email@email.com"), new Password("dsadasd"), "lucas");
        User newUser2Diff = new User(new Email("emaaail@email.com"), new Password("dsadasd"), "lucas");

        String notUser = "not User";

        assertEquals(newUser, newUser); //same obj
        assertEquals(newUser2, newUser); //same attribute of comparison
        assertNotEquals(newUser, notUser); //diff class
        assertNotEquals(newUser, null); //null
        assertNotEquals(newUser, newUser2Diff); //null
    }

}