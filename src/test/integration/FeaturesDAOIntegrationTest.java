package test.integration;

import main.db.FeaturesDAO;
import main.model.Card;
import main.model.Player;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeaturesDAOIntegrationTest
        extends DAOIntegrationTestSupport {

    @Test
    void save_persistsDerivedFeatureValues()
            throws SQLException {

        // ARRANGE
        long simulationId =
                insertTestSimulation(2);

        Card aceClub =
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.CLUB
                );

        Card kingHeart =
                new Card(
                        Card.Rank.KING,
                        Card.Suit.HEART
                );

        long playerId =
                insertTestPlayer(
                        simulationId,
                        1,
                        aceClub,
                        kingHeart
                );

        Player player =
                new Player(
                        "player1",
                        new ArrayList<>(
                                List.of(
                                        aceClub,
                                        kingHeart
                                )
                        )
                );

        FeaturesDAO dao =
                new FeaturesDAO(cn);


        // ACT
        dao.save(
                player,
                simulationId,
                playerId,
                1,
                2
        );


        // ASSERT
        String sql = """
                SELECT
                    simulation_id,
                    simulation_player_id,
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
                WHERE simulation_player_id = ?
                """;

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(1, playerId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                assertTrue(rs.next());

                assertEquals(
                        simulationId,
                        rs.getLong("simulation_id")
                );

                assertEquals(
                        playerId,
                        rs.getLong(
                                "simulation_player_id"
                        )
                );

                assertEquals(
                        "AKo",
                        rs.getString(
                                "hole_card_category"
                        )
                );

                assertFalse(
                        rs.getBoolean("is_pair")
                );

                assertFalse(
                        rs.getBoolean("is_suited")
                );

                assertTrue(
                        rs.getBoolean(
                                "is_connected"
                        )
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
    }


    @Test
    void save_rejectsInvalidHoleCardCount()
            throws SQLException {

        // ARRANGE
        long simulationId =
                insertTestSimulation(2);

        Card aceClub =
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.CLUB
                );

        Card kingHeart =
                new Card(
                        Card.Rank.KING,
                        Card.Suit.HEART
                );

        long playerId =
                insertTestPlayer(
                        simulationId,
                        1,
                        aceClub,
                        kingHeart
                );

        Player invalidPlayer =
                new Player(
                        "player1",
                        new ArrayList<>(
                                List.of(aceClub)
                        )
                );

        FeaturesDAO dao =
                new FeaturesDAO(cn);


        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(
                        invalidPlayer,
                        simulationId,
                        playerId,
                        1,
                        2
                )
        );

        assertEquals(
                0,
                countRows("features")
        );
    }
}