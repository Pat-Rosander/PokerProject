package test.integration;

import main.db.PlayerDAO;
import main.model.Card;
import main.model.Player;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerDAOIntegrationTest
        extends DAOIntegrationTestSupport {

    @Test
    void save_persistsPlayerAndReturnsGeneratedId()
            throws SQLException {

        // ARRANGE
        long simulationId =
                insertTestSimulation(2);

        Card card1 =
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.CLUB
                );

        Card card2 =
                new Card(
                        Card.Rank.KING,
                        Card.Suit.HEART
                );

        Player player =
                new Player(
                        "player1",
                        new ArrayList<>(
                                List.of(card1, card2)
                        )
                );

        PlayerDAO dao =
                new PlayerDAO(cn);


        // ACT
        long simulationPlayerId =
                dao.save(
                        player,
                        simulationId,
                        1
                );


        // ASSERT
        assertTrue(simulationPlayerId > 0);

        String sql = """
                SELECT
                    simulation_id,
                    player_position,
                    hole_card_1_rank,
                    hole_card_1_suit,
                    hole_card_2_rank,
                    hole_card_2_suit
                FROM players
                WHERE simulation_player_id = ?
                """;

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(
                    1,
                    simulationPlayerId
            );

            try (ResultSet rs =
                         ps.executeQuery()) {

                assertTrue(rs.next());

                assertEquals(
                        simulationId,
                        rs.getLong("simulation_id")
                );

                assertEquals(
                        1,
                        rs.getInt("player_position")
                );

                assertEquals(
                        "ACE",
                        rs.getString(
                                "hole_card_1_rank"
                        )
                );

                assertEquals(
                        "CLUB",
                        rs.getString(
                                "hole_card_1_suit"
                        )
                );

                assertEquals(
                        "KING",
                        rs.getString(
                                "hole_card_2_rank"
                        )
                );

                assertEquals(
                        "HEART",
                        rs.getString(
                                "hole_card_2_suit"
                        )
                );

                assertFalse(rs.next());
            }
        }
    }


    @Test
    void save_rejectsNonexistentSimulationForeignKey() {

        // ARRANGE
        PlayerDAO dao =
                new PlayerDAO(cn);

        Player player =
                new Player(
                        "player1",
                        new ArrayList<>(
                                List.of(
                                        new Card(
                                                Card.Rank.ACE,
                                                Card.Suit.CLUB
                                        ),
                                        new Card(
                                                Card.Rank.KING,
                                                Card.Suit.HEART
                                        )
                                )
                        )
                );

        long nonexistentSimulationId = -999;


        // ACT + ASSERT
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dao.save(
                                player,
                                nonexistentSimulationId,
                                1
                        )
                );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }
}