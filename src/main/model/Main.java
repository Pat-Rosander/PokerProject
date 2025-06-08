package main.model;

import main.db.*;
import main.simulation.*;

import java.sql.*;

public class Main {
    // Test connection
    public static void main(String[] args) {
        /*
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
         */
        PokerSimulation simulation = new PokerSimulation();
        SimulationResults results = simulation.runSimulation(2);
        System.out.println(simulation.toString());
        for (int i = 0; i < simulation.getWinningPlayers().size(); i++) {
            System.out.println(simulation.getWinningPlayers().get(i).getPlayerResults());
        }
    }
}