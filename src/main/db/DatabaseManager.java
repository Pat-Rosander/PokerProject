package main.db;

import java.sql.*;

/**
 * DatabaseManager contains CRUD database methods. Creates different tables simulations, players,
 * hands, and community_cards.
 */
public class DatabaseManager {
    /**
     * Connects to database
     * @return
     */
    public static Connection getConnection() {
        try {
            // Environment variables stored on local OS to prevent hardcoding/embedding passwords in source code
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");
            if (url == null || user == null || pass == null) {
                throw new IllegalStateException("Database environment variables are not set properly");
            }

            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database",e);
        }
    }
    /**
     * Calls all table creation methods in sequence
     * @param connection
     */
    public static void initializeSchema(Connection connection) {
        createSimulationTable(connection);
        createPlayersTable(connection);
        createOutcomesTable(connection);
        createCommunityCardsTable(connection);
    }
    /**
     * Creates table storing simulation
     * @param connection
     */
    public static void createSimulationTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            final String createTableStatement = """
                CREATE TABLE IF NOT EXISTS simulations (
                    simulation_id BIGINT PRIMARY KEY AUTO_INCREMENT, 
                    num_players INT NOT NULL,
                    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    simulation_type VARCHAR(50),
                    notes TEXT
                );
                """;
            statement.execute(createTableStatement);
            System.out.println("Simulation table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create simulation table", e);
        }
    }
    /**
     * Creates table storing player info
     * @param connection
     */
    public static void createPlayersTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            final String createTableStatement = """
                CREATE TABLE IF NOT EXISTS players (
                    simulation_player_id BIGINT PRIMARY KEY AUTO_INCREMENT, 
                    simulation_id BIGINT NOT NULL,
                    player_position INT NOT NULL,
                    
                    hole_card_1_rank VARCHAR(10) NOT NULL,
                    hole_card_1_suit VARCHAR(10) NOT NULL,
                    hole_card_2_rank VARCHAR(10) NOT NULL,
                    hole_card_2_suit VARCHAR(10) NOT NULL,
                    
                    FOREIGN KEY (simulation_id) REFERENCES simulations(simulation_id)
            );
            """;
            statement.execute(createTableStatement);
            System.out.println("Players table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create players table", e);
        }
    }

    /**
     * Creates table storing community cards
     * @param connection
     */
    public static void createCommunityCardsTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            final String createTableStatement = """
                CREATE TABLE IF NOT EXISTS community_cards (
                    community_cards_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    simulation_id BIGINT NOT NULL,
                    
                    flop_1_rank VARCHAR(10),
                    flop_1_suit VARCHAR(10),
                    flop_2_rank VARCHAR(10),
                    flop_2_suit VARCHAR(10),
                    flop_3_rank VARCHAR(10),
                    flop_3_suit VARCHAR(10),
                    turn_rank VARCHAR(10),
                    turn_suit VARCHAR(10),
                    river_rank VARCHAR(10),
                    river_suit VARCHAR(10),
                    
                    FOREIGN KEY (simulation_id) REFERENCES simulations(simulation_id)
                );
                """;
            statement.execute(createTableStatement);
            System.out.println("Community cards table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create community_cards table", e);
        }
    }

    /**
     * Creates table storing simulation results
     * @param connection
     */
    public static void createOutcomesTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            final String createTableStatement = """
                CREATE TABLE IF NOT EXISTS outcomes (
                    outcome_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    simulation_id BIGINT NOT NULL,
                    simulation_player_id BIGINT NOT NULL,
                    
                    hand_rank VARCHAR(30) NOT NULL,
                    hand_strength INT NOT NULL,
                    is_winner BOOLEAN NOT NULL,
                    
                    best_card_1_rank VARCHAR(10),
                    best_card_1_suit VARCHAR(10),
                    best_card_2_rank VARCHAR(10),
                    best_card_2_suit VARCHAR(10),
                    best_card_3_rank VARCHAR(10),
                    best_card_3_suit VARCHAR(10),
                    best_card_4_rank VARCHAR(10),
                    best_card_4_suit VARCHAR(10),
                    best_card_5_rank VARCHAR(10),
                    best_card_5_suit VARCHAR(10),
                    
                    FOREIGN KEY (simulation_id) REFERENCES simulations(simulation_id),
                    FOREIGN KEY (simulation_player_id) REFERENCES simulation_players(simulation_player_id)
                );
                """;
            statement.execute(createTableStatement);
            System.out.println("Outcomes table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create outcomes table", e);
        }
    }

    /**
     * Creates table storing analytics for machine learning data
     * @param connection
     */
    public static void createFeaturesTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            final String createTableStatement = """
                CREATE TABLE IF NOT EXISTS features (
                    feature_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    simulation_id BIGINT NOT NULL,
                    simulation_player_id BIGINT NOT NULL,
                
                    hole_card_category VARCHAR(10),
                    is_pair BOOLEAN,
                    is_suited BOOLEAN,
                    is_connected BOOLEAN,
                    rank_gap INT,
                    high_card_rank INT,
                    low_card_rank INT,
                
                    player_count INT,
                    position INT,
                
                    FOREIGN KEY (simulation_id) REFERENCES simulations(simulation_id),
                    FOREIGN KEY (simulation_player_id) REFERENCES simulation_players(simulation_player_id)
                );
                """;
            statement.execute(createTableStatement);
            System.out.println("Feature table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create features table", e);
        }
    }
}
