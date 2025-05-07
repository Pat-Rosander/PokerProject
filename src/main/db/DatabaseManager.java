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
        createHandsTable(connection);
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
                    id SERIAL PRIMARY KEY, 
                    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
                    id SERIAL PRIMARY KEY, 
                    simulation_id INT REFERENCES simulations(id) ON DELETE CASCADE, 
                    name VARCHAR(50), 
                    card1 VARCHAR(20), 
                    card2 VARCHAR(20), 
                    is_winner BOOLEAN
            );
            """;
            statement.execute(createTableStatement);
            System.out.println("Players table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create players table", e);
        }
    }
    /**
     * Creates table storing hand info
     * @param connection
     */
    public static void createHandsTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            // Create ENUM type if doesn't exist
            final String createEnumStatement = """
                DO $$ BEGIN
                    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'hand_rank_enum') THEN
                        CREATE TYPE hand_rank_enum AS ENUM (
                            'HIGH_CARD', 'PAIR', 'TWO_PAIR', 'THREE_OF_A_KIND', 'STRAIGHT',
                            'FLUSH', 'FULL_HOUSE', 'FOUR_OF_A_KIND', 'STRAIGHT_FLUSH', 'ROYAL_FLUSH'
                        );
                    END IF;
                END $$;
                """;
            statement.execute(createEnumStatement);

            final String createTableStatement = """
                CREATE TABLE IF NOT EXISTS hands (
                    id SERIAL PRIMARY KEY, 
                    player_id INT references players(id), 
                    hand_rank hand_rank_enum NOT NULL, 
                    hand_strength INT NOT NULL, 
                    cards JSONB
                );
                """;
            statement.execute(createTableStatement);
            System.out.println("Hands table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create hands table", e);
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
                    id SERIAL PRIMARY KEY, 
                    simulation_id INT REFERENCES simulations(id), 
                    cards JSONB
                );
                """;
            statement.execute(createTableStatement);
            System.out.println("Community cards table created");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create community_cards table", e);
        }
    }
}
