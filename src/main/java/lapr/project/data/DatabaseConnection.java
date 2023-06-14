/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author nunocastro
 */
public class DatabaseConnection {
    private OracleDataSource oracleDataSource;
    private Connection connection;
    private SQLException error;

    public DatabaseConnection(String url, String username, String password) {
        try {
            oracleDataSource = new OracleDataSource();

            oracleDataSource.setURL(url);

            connection = oracleDataSource.getConnection(username, password);

        } catch (SQLException e) {
            Logger.getLogger(DatabaseConnection.class.getName())
                    .log(Level.SEVERE, null, e);
            System.err.format("SQL State: %s\n%s", e.getSQLState(),
                    e.getMessage());
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new RuntimeException("Connection does not exit");
        }
        return connection;
    }

    public void registerError(SQLException error) {
        this.error = error;
    }

    public SQLException getLastError() {
        SQLException lastError = this.error;
        //Clear after reading.
        registerError(null);
        return lastError;
    }
}