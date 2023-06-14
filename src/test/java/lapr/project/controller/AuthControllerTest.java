package lapr.project.controller;

import auth.UserSession;
import lapr.project.domain.model.Company;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    AuthController authController;
    App app;

    @Test
    public void checkBootstrappedLoginAndLogout(){
        app = App.getInstance();
        authController = new AuthController();
        assertTrue(authController.doLogin("trafficm@mail.com", "123456"));
        authController.doLogout();
    }
    @Test
    public void checkFailedLoginNotRegistered(){
        authController = new AuthController();
        assertFalse(authController.doLogin("notnot@mail.com", "123456"));
    }

    @Test
    public void checkFailedLoginInvalidChar(){
        authController = new AuthController();
        assertFalse(authController.doLogin("lala@lalal..com", "123456"));
    }

    @Test
    public void checkFailedLoginInvalidCharBlank(){
        authController = new AuthController();
        assertFalse(authController.doLogin("", "123456"));
    }

    @Test
    public void testGetCompany(){
        app = App.getInstance();
        Company c = app.getCompany();
        c.getAuthFacade().addUser("Name", "email@gmail.com", "12334556");
        UserSession us = c.getAuthFacade().doLogin("email@gmail.com", "12334556");
        assertEquals(app.getCurrentUserSession().getUserId().getEmail(), us.getUserId().getEmail());
    }


}