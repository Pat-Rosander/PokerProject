package test.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

// Final class means cannot be inherited or extend
public final class TestDatabaseSetup {

    private static final String EXPECTED_DB = "pokertest";

    private TestDatabaseSetup() {

    }

    public static void verifyTestDatabase(Connection connection)
            throws SQLException {

            if(!EXPECTED_DB.equals(connection.getCatalog())) {
                throw new IllegalStateException(
                        "Persistence tests must only run against " + EXPECTED_DB +
                                ". Connected to: " + connection.getCatalog()
                );
            }

    }


    public static void clearDatabase(Connection connection)
            throws SQLException {

        verifyTestDatabase(connection);

        String sql = """
                TRUNCATE TABLE
                    outcomes,
                    features,
                    community_cards,
                    players,
                    simulations
                RESTART IDENTITY CASCADE;
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

}
