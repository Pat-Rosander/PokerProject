package test.integration;

import main.db.DatabaseManager;
import main.model.Card;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import test.db.TestDatabaseSetup;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class DAOIntegrationTestSupport {

    protected Connection cn;

    @BeforeEach
    void setUpDatabase() throws SQLException {
        cn = DatabaseManager.getConnection();

        TestDatabaseSetup.clearDatabase(cn);
    }

    @AfterEach
    void closeDatabase() throws SQLException {
        if (cn != null && !cn.isClosed()) {
            cn.close();
        }
    }


    protected long insertTestSimulation(int numPlayers)
            throws SQLException {

        String sql = """
                INSERT INTO simulations (
                    num_players,
                    simulation_type
                )
                VALUES (?, ?)
                RETURNING simulation_id
                """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numPlayers);
            ps.setString(2, "MONTE CARLO");

            try (ResultSet rs = ps.executeQuery()) {

                assertTrue(rs.next());

                return rs.getLong("simulation_id");
            }
        }
    }


    protected long insertTestPlayer(
            long simulationId,
            int position,
            Card card1,
            Card card2
    ) throws SQLException {

        String sql = """
                INSERT INTO players (
                    simulation_id,
                    player_position,
                    hole_card_1_rank,
                    hole_card_1_suit,
                    hole_card_2_rank,
                    hole_card_2_suit
                )
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING simulation_player_id
                """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, simulationId);
            ps.setInt(2, position);

            ps.setString(3, card1.getRank().name());
            ps.setString(4, card1.getSuit().name());

            ps.setString(5, card2.getRank().name());
            ps.setString(6, card2.getSuit().name());

            try (ResultSet rs = ps.executeQuery()) {

                assertTrue(rs.next());

                return rs.getLong(
                        "simulation_player_id"
                );
            }
        }
    }


    protected int countRows(String tableName)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM " + tableName;

        try (Statement statement = cn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            rs.next();

            return rs.getInt(1);
        }
    }
}