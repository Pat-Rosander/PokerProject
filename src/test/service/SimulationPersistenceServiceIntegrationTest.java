package test.service;

import main.db.DatabaseManager;

import main.model.Player;
import main.model.Card;

import main.simulation.HandResult;
import main.simulation.HandRank;
import main.simulation.SimulationResults;

import main.service.SimulationPersistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.db.TestDatabaseSetup;

import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        SimulationResults results = createValidSimulationResults();

        persistence.saveSimulationResults(results);

        assertEquals(1, countRows("simulations"));
        assertEquals(2, countRows("players"));
        assertEquals(2, countRows("features"));
        assertEquals(2, countRows("outcomes"));
        assertEquals(1, countRows("community_cards"));
    }

    // 3. SIMULATION FOREIGN-KEY RELATIONSHIPS
    // ============================================================

    @Test
    void saveSimulationResults_preservesSimulationRelationships()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert
        long simulationId = getOnlySimulationId();

        assertEquals(
                2,
                countRowsForSimulation("players", simulationId)
        );

        assertEquals(
                2,
                countRowsForSimulation("features", simulationId)
        );

        assertEquals(
                2,
                countRowsForSimulation("outcomes", simulationId)
        );

        assertEquals(
                1,
                countRowsForSimulation("community_cards", simulationId)
        );
    }


    // ============================================================
    // 4. PLAYER -> FEATURES / OUTCOMES RELATIONSHIPS
    // ============================================================

    @Test
    void saveSimulationResults_preservesPlayerRelationships()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert
        String sql = """
                SELECT simulation_player_id
                FROM players
                ORDER BY player_position
                """;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            int playersChecked = 0;

            while (rs.next()) {
                long simulationPlayerId =
                        rs.getLong("simulation_player_id");

                assertEquals(
                        1,
                        countRowsForPlayer(
                                "features",
                                simulationPlayerId
                        )
                );

                assertEquals(
                        1,
                        countRowsForPlayer(
                                "outcomes",
                                simulationPlayerId
                        )
                );

                playersChecked++;
            }

            assertEquals(2, playersChecked);
        }
    }


    // ============================================================
    // 5. SIMULATION METADATA
    // ============================================================

    @Test
    void saveSimulationResults_persistsSimulationMetadata()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert
        String sql = """
                SELECT num_players, simulation_type
                FROM simulations
                """;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            assertTrue(rs.next());

            assertEquals(
                    2,
                    rs.getInt("num_players")
            );

            assertEquals(
                    "MONTE CARLO",
                    rs.getString("simulation_type")
            );

            // There should only be one simulation.
            assertFalse(rs.next());
        }
    }


    // ============================================================
    // 6. PLAYER DATA
    // ============================================================

    @Test
    void saveSimulationResults_persistsPlayerData()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert
        String sql = """
                SELECT
                    player_position,
                    hole_card_1_rank,
                    hole_card_1_suit,
                    hole_card_2_rank,
                    hole_card_2_suit
                FROM players
                ORDER BY player_position
                """;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            // Player 1
            assertTrue(rs.next());

            assertEquals(1, rs.getInt("player_position"));
            assertEquals("ACE", rs.getString("hole_card_1_rank"));
            assertEquals("CLUB", rs.getString("hole_card_1_suit"));
            assertEquals("KING", rs.getString("hole_card_2_rank"));
            assertEquals("HEART", rs.getString("hole_card_2_suit"));


            // Player 2
            assertTrue(rs.next());

            assertEquals(2, rs.getInt("player_position"));
            assertEquals("EIGHT", rs.getString("hole_card_1_rank"));
            assertEquals("CLUB", rs.getString("hole_card_1_suit"));
            assertEquals("FOUR", rs.getString("hole_card_2_rank"));
            assertEquals("DIAMOND", rs.getString("hole_card_2_suit"));


            // No third player
            assertFalse(rs.next());
        }
    }


    // ============================================================
    // 7. DERIVED FEATURE DATA
    // ============================================================

    @Test
    void saveSimulationResults_persistsDerivedFeatures()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert Player 1:
        // Ace Clubs + King Hearts
        //
        // AKo
        // not pair
        // not suited
        // connected
        // rank gap = 1
        // high = 14
        // low = 13

        String sql = """
                SELECT
                    hole_card_category,
                    is_pair,
                    is_suited,
                    is_connected,
                    rank_gap,
                    high_card_rank,
                    low_card_rank,
                    player_count,
                    position
                FROM features
                WHERE position = 1
                """;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            assertTrue(rs.next());

            assertEquals(
                    "AKo",
                    rs.getString("hole_card_category")
            );

            assertFalse(
                    rs.getBoolean("is_pair")
            );

            assertFalse(
                    rs.getBoolean("is_suited")
            );

            assertTrue(
                    rs.getBoolean("is_connected")
            );

            assertEquals(
                    1,
                    rs.getInt("rank_gap")
            );

            assertEquals(
                    14,
                    rs.getInt("high_card_rank")
            );

            assertEquals(
                    13,
                    rs.getInt("low_card_rank")
            );

            assertEquals(
                    2,
                    rs.getInt("player_count")
            );

            assertEquals(
                    1,
                    rs.getInt("position")
            );

            assertFalse(rs.next());
        }
    }


    // ============================================================
    // 8. OUTCOME DATA
    // ============================================================

    @Test
    void saveSimulationResults_persistsOutcomeData()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        /*
         * Join outcomes to players so we can identify Player 1
         * using player_position.
         */
        String sql = """
                SELECT
                    o.hand_rank,
                    o.hand_strength,
                    o.is_winner,
                    o.best_card_1_rank,
                    o.best_card_1_suit,
                    o.best_card_2_rank,
                    o.best_card_2_suit,
                    o.best_card_3_rank,
                    o.best_card_3_suit,
                    o.best_card_4_rank,
                    o.best_card_4_suit,
                    o.best_card_5_rank,
                    o.best_card_5_suit
                FROM outcomes o
                JOIN players p
                    ON o.simulation_player_id =
                       p.simulation_player_id
                WHERE p.player_position = 1
                """;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            assertTrue(rs.next());

            assertEquals(
                    "PAIR",
                    rs.getString("hand_rank")
            );

            assertEquals(
                    2,
                    rs.getInt("hand_strength")
            );

            assertTrue(
                    rs.getBoolean("is_winner")
            );

            assertEquals(
                    "KING",
                    rs.getString("best_card_1_rank")
            );

            assertEquals(
                    "HEART",
                    rs.getString("best_card_1_suit")
            );

            assertEquals(
                    "KING",
                    rs.getString("best_card_2_rank")
            );

            assertEquals(
                    "CLUB",
                    rs.getString("best_card_2_suit")
            );

            assertEquals(
                    "ACE",
                    rs.getString("best_card_3_rank")
            );

            assertEquals(
                    "CLUB",
                    rs.getString("best_card_3_suit")
            );

            assertEquals(
                    "JACK",
                    rs.getString("best_card_4_rank")
            );

            assertEquals(
                    "SPADE",
                    rs.getString("best_card_4_suit")
            );

            assertEquals(
                    "NINE",
                    rs.getString("best_card_5_rank")
            );

            assertEquals(
                    "HEART",
                    rs.getString("best_card_5_suit")
            );

            assertFalse(rs.next());
        }
    }


    // ============================================================
    // 9. COMMUNITY CARD DATA
    // ============================================================

    @Test
    void saveSimulationResults_persistsCommunityCards()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert
        String sql = """
                SELECT *
                FROM community_cards
                """;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            assertTrue(rs.next());

            assertEquals(
                    "TWO",
                    rs.getString("flop_1_rank")
            );

            assertEquals(
                    "CLUB",
                    rs.getString("flop_1_suit")
            );

            assertEquals(
                    "SEVEN",
                    rs.getString("flop_2_rank")
            );

            assertEquals(
                    "DIAMOND",
                    rs.getString("flop_2_suit")
            );

            assertEquals(
                    "NINE",
                    rs.getString("flop_3_rank")
            );

            assertEquals(
                    "HEART",
                    rs.getString("flop_3_suit")
            );

            assertEquals(
                    "JACK",
                    rs.getString("turn_rank")
            );

            assertEquals(
                    "SPADE",
                    rs.getString("turn_suit")
            );

            assertEquals(
                    "KING",
                    rs.getString("river_rank")
            );

            assertEquals(
                    "CLUB",
                    rs.getString("river_suit")
            );

            assertFalse(rs.next());
        }
    }


    // ============================================================
    // 10. SUCCESSFUL TRANSACTION IS COMMITTED
    // ============================================================

    @Test
    void saveSimulationResults_commitsSuccessfulTransaction()
            throws SQLException {

        // Arrange
        SimulationResults results = createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        /*
         * Use a SECOND connection.
         *
         * This is important because cn could potentially see its
         * own transaction's work. A different connection proves
         * PostgreSQL received a COMMIT.
         */
        try (Connection secondConnection =
                     DatabaseManager.getConnection()) {

            TestDatabaseSetup.verifyTestDatabase(
                    secondConnection
            );

            String sql =
                    "SELECT COUNT(*) FROM simulations";

            try (Statement statement =
                         secondConnection.createStatement();

                 ResultSet rs =
                         statement.executeQuery(sql)) {

                assertTrue(rs.next());

                assertEquals(
                        1,
                        rs.getInt(1)
                );
            }
        }
    }


    // ============================================================
    // 11. ROLLBACK ON FAILURE
    // ============================================================

    @Test
    void saveSimulationResults_rollsBackEntireTransactionOnFailure()
            throws SQLException {

        // Arrange
        SimulationResults invalidResults =
                createInvalidSimulationResults();

        // Act
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        persistence.saveSimulationResults(
                                invalidResults
                        )
        );

        // Assert:
        // nothing from the failed transaction should remain.

        assertEquals(0, countRows("simulations"));
        assertEquals(0, countRows("players"));
        assertEquals(0, countRows("features"));
        assertEquals(0, countRows("outcomes"));
        assertEquals(0, countRows("community_cards"));
    }


    // ============================================================
    // 12. AUTOCOMMIT RESTORED AFTER SUCCESS
    // ============================================================

    @Test
    void saveSimulationResults_restoresAutoCommitAfterSuccess()
            throws SQLException {

        // Arrange
        boolean originalAutoCommit =
                cn.getAutoCommit();

        SimulationResults results =
                createValidSimulationResults();

        // Act
        persistence.saveSimulationResults(results);

        // Assert
        assertEquals(
                originalAutoCommit,
                cn.getAutoCommit()
        );
    }


    // ============================================================
    // 13. AUTOCOMMIT RESTORED AFTER FAILURE
    // ============================================================

    @Test
    void saveSimulationResults_restoresAutoCommitAfterRollback()
            throws SQLException {

        // Arrange
        boolean originalAutoCommit =
                cn.getAutoCommit();

        SimulationResults invalidResults =
                createInvalidSimulationResults();

        // Act
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        persistence.saveSimulationResults(
                                invalidResults
                        )
        );

        // Assert
        assertEquals(
                originalAutoCommit,
                cn.getAutoCommit()
        );
    }

    private int countRows(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            rs.next();
            return rs.getInt(1);
        }
    }


    private SimulationResults createValidSimulationResults() {

        // -------------------------
        // Community cards
        // -------------------------
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.TWO, Card.Suit.CLUB),
                new Card(Card.Rank.SEVEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.NINE, Card.Suit.HEART),
                new Card(Card.Rank.JACK, Card.Suit.SPADE),
                new Card(Card.Rank.KING, Card.Suit.CLUB)
        ));


        // -------------------------
        // Player 1
        // -------------------------
        ArrayList<Card> player1HoleCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.CLUB),
                new Card(Card.Rank.KING, Card.Suit.HEART)
        ));

        Player player1 = new Player(
                "player1",
                player1HoleCards
        );


        // Player 1 has a pair of kings:
        // K♥ K♣ A♣ J♠ 9♥
        ArrayList<Card> player1BestFive = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.CLUB),
                new Card(Card.Rank.ACE, Card.Suit.CLUB),
                new Card(Card.Rank.JACK, Card.Suit.SPADE),
                new Card(Card.Rank.NINE, Card.Suit.HEART)
        ));

        HandResult player1Result = new HandResult(
                HandRank.PAIR,
                player1BestFive,
                2
        );

        player1.setPlayerResults(player1Result);


        // -------------------------
        // Player 2
        // -------------------------
        ArrayList<Card> player2HoleCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.EIGHT, Card.Suit.CLUB),
                new Card(Card.Rank.FOUR, Card.Suit.DIAMOND)
        ));

        Player player2 = new Player(
                "player2",
                player2HoleCards
        );


        // Player 2 has high card:
        // K♣ J♠ 9♥ 8♣ 7♦
        ArrayList<Card> player2BestFive = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.CLUB),
                new Card(Card.Rank.JACK, Card.Suit.SPADE),
                new Card(Card.Rank.NINE, Card.Suit.HEART),
                new Card(Card.Rank.EIGHT, Card.Suit.CLUB),
                new Card(Card.Rank.SEVEN, Card.Suit.DIAMOND)
        ));

        HandResult player2Result = new HandResult(
                HandRank.HIGH_CARD,
                player2BestFive,
                1
        );

        player2.setPlayerResults(player2Result);


        // -------------------------
        // Simulation participants
        // -------------------------
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(
                player1,
                player2
        ));

        // Player 1 is the winner.
        ArrayList<Player> winningPlayers = new ArrayList<>(List.of(
                player1
        ));


        // SimulationResults constructor will mark
        // players contained in winningPlayers as winners.
        return new SimulationResults(
                players,
                winningPlayers,
                communityCards
        );
    }

    // ============================================================
    // HELPER: INVALID FIXTURE FOR ROLLBACK TEST
    // ============================================================

    private SimulationResults createInvalidSimulationResults() {

        /*
         * Start with the valid object graph.
         */
        SimulationResults results =
                createValidSimulationResults();

        /*
         * Break Player 2's outcome AFTER Player 1 remains valid.
         *
         * This is intentional:
         *
         * Simulation row will already have been inserted.
         * Player 1 will already have been inserted.
         * Player 1 outcome/features will already have been inserted.
         * Player 2 row will already have been inserted.
         *
         * Then OutcomesDAO encounters only 4 best cards
         * and throws IllegalArgumentException.
         *
         * SimulationPersistence should rollback ALL earlier inserts.
         */
        Player player2 =
                results.getPlayersList().get(1);


        ArrayList<Card> invalidBestFive =
                new ArrayList<>(Arrays.asList(

                        new Card(
                                Card.Rank.KING,
                                Card.Suit.CLUB
                        ),

                        new Card(
                                Card.Rank.JACK,
                                Card.Suit.SPADE
                        ),

                        new Card(
                                Card.Rank.NINE,
                                Card.Suit.HEART
                        ),

                        new Card(
                                Card.Rank.EIGHT,
                                Card.Suit.CLUB
                        )
                ));


        player2.setPlayerResults(
                new HandResult(
                        HandRank.HIGH_CARD,
                        invalidBestFive,
                        1
                )
        );


        return results;
    }

    private long getOnlySimulationId()
            throws SQLException {

        String sql =
                "SELECT simulation_id FROM simulations";

        try (Statement statement =
                     cn.createStatement();

             ResultSet rs =
                     statement.executeQuery(sql)) {

            assertTrue(
                    rs.next(),
                    "Expected one simulation row"
            );

            long simulationId =
                    rs.getLong("simulation_id");

            assertFalse(
                    rs.next(),
                    "Expected only one simulation row"
            );

            return simulationId;
        }
    }


    private int countRowsForSimulation(
            String tableName,
            long simulationId
    ) throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM "
                        + tableName
                        + " WHERE simulation_id = ?";

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    simulationId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                rs.next();

                return rs.getInt(1);
            }
        }
    }


    private int countRowsForPlayer(
            String tableName,
            long simulationPlayerId
    ) throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM "
                        + tableName
                        + " WHERE simulation_player_id = ?";

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    simulationPlayerId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                rs.next();

                return rs.getInt(1);
            }
        }
    }

}
