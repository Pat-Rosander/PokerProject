package main.model;

import main.db.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        try {
            System.out.println("Connecting to DB");
            Connection conn = db.getConnection();
            System.out.println("Connected!");
            conn.close();
        } catch (Exception e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
}