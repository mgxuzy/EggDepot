package nu.eats.feature.transaction.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {
    private static final String DB_URL_NO_DB = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_URL_WITH_DB = "jdbc:mysql://localhost:3306/nueats?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private static final DatabaseManager INSTANCE = new DatabaseManager();

    private boolean available = false;

    private DatabaseManager() {
        // Load MySQL driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseManager] MySQL Driver not found: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public boolean isAvailable() {
        return available;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL_WITH_DB, USER, PASSWORD);
    }

    public void initialize() {
        // 1. Try to connect without DB first and create database if not exists
        try (Connection conn = DriverManager.getConnection(DB_URL_NO_DB, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS nueats");
            System.out.println("[DatabaseManager] Database 'nueats' checked/created successfully.");
            available = true;
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Failed to connect or create database: " + e.getMessage());
            available = false;
            return;
        }

        // 2. Connect with DB and create the table
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createTableSql = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "product VARCHAR(255) NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "price DOUBLE NOT NULL, " +
                    "total DOUBLE NOT NULL, " +
                    "date DATETIME NOT NULL" +
                    ")";
            stmt.executeUpdate(createTableSql);
            System.out.println("[DatabaseManager] Table 'transactions' checked/created successfully.");
            available = true;
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Failed to initialize transactions table: " + e.getMessage());
            available = false;
        }
    }
}
