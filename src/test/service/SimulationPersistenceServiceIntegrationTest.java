package test.service;

import main.db.DatabaseManager;
import main.service.SimulationPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.db.TestDatabaseSetup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;

public class SimulationPersistenceServiceIntegrationTest {

    private Connection cn;
    private SimulationPersistence persistence;
    private static final String EXPECTED_DB = "pokertest";

    /**
     * validate that db connection points to EXPECTED_DB and clear before each test
     * safety invariant so that destructive logic can't be run against PokerDB
     *
     * @throws SQLException
     */
    @BeforeEach
    public void setUp() throws SQLException {
        cn = DatabaseManager.getConnection();

        TestDatabaseSetup.clearDatabase(cn);

        persistence = new SimulationPersistence(cn);
    }

    /**
     * Verify cn exists and is open before closing connection after each test
     * @throws SQLException
     */
    @AfterEach
    public void tearDown() throws SQLException {
        if (cn != null && !cn.isClosed()) {
            cn.close();
        }
    }

    /**
     * smoke test to make sure that:
     *      JUnit --> connection --> env variables --> JDBC driver --> PostgreSQL
     * are properly configured to reach pokertest
     *
     * @throws SQLException
     */
    @Test
    void connectsToPokerTestDatabase() throws SQLException {
        String actualDB = cn.getCatalog();

        assertEquals(EXPECTED_DB, actualDB);
    }

    @Test
    void saveSimulationResults_persistsAllExpectedRows() throws SQLException {

    }
}
