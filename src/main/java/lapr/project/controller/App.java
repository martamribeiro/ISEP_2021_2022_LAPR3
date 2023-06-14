package lapr.project.controller;

import lapr.project.data.ConnectionFactory;
import lapr.project.data.DatabaseConnection;
import lapr.project.domain.model.Company;
import auth.AuthFacade;
import auth.UserSession;
import lapr.project.domain.shared.Constants;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ana Albergaria <1201518@isep.ipp.pt> inspired in class by: Paulo Maio <pam@isep.ipp.pt>
 */
public class App {

    private Company company;
    private AuthFacade authFacade;
    private DatabaseConnection databaseConnection;
    private App()
    {
        this.company = new Company(Constants.PARAMS_COMPANY_DESIGNATION);
        this.authFacade = this.company.getAuthFacade();
        bootstrap();
    }

    public Company getCompany()
    {
        return this.company;
    }

    private void dataBaseConnection(){
        try {
            databaseConnection = ConnectionFactory.getInstance().getDatabaseConnection();
        } catch (IOException exception) {
            Logger.getLogger("db")
                    .log(Level.SEVERE, null, exception);
        }

        System.out.println("Connected to the database!");
    }

    public DatabaseConnection getConnection(){
        if(databaseConnection == null){
                dataBaseConnection();
        }
        return databaseConnection;
    }

    public UserSession getCurrentUserSession()
    {
        return this.authFacade.getCurrentUserSession();
    }

    public boolean doLogin(String email, String pwd)
    {
        return this.authFacade.doLogin(email,pwd).isLoggedIn();
    }

    public void doLogout()
    {
        this.authFacade.doLogout();
    }

    private void bootstrap()
    {
        this.authFacade.addUserRole(Constants.ROLE_TRAFFIC_MANAGER,Constants.ROLE_TRAFFIC_MANAGER);
        this.authFacade.addUserWithRole("Traffic Manager", "trafficm@mail.com", "123456",Constants.ROLE_TRAFFIC_MANAGER);
    }

    // Extracted from https://www.javaworld.com/article/2073352/core-java/core-java-simply-singleton.html?page=2
    private static App singleton = null;
    public static App getInstance()
    {
        if(singleton == null)
        {
            synchronized(App.class)
            {
                singleton = new App();
            }
        }
        return singleton;
    }
}
