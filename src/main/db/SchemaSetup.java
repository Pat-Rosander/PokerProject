package main.db;

import java.sql.Connection;

public class SchemaSetup {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        try (Connection connection = db.getConnection()) {
            db.initializeSchema(connection);
            System.out.println("Schema initialized");
        } catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
}
