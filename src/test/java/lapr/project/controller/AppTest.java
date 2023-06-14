package lapr.project.controller;

import lapr.project.data.DatabaseConnection;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AppTest {

    @Disabled
    @Test
    public void testDbConnection(){
        App app = App.getInstance();
        DatabaseConnection connection = app.getConnection();
    }
}