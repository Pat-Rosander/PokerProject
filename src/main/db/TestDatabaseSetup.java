package main.db;

import java.sql.Connection;

public class TestDatabaseSetup {
    public static void main(String[] args) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            DatabaseManager.initializeSchema(conn);
            System.out.println("Schema has been initialized");
        }
    }
}
