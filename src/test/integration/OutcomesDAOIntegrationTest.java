package test.integration;

import main.db.OutcomesDAO;
import main.model.Card;
import main.model.Player;
import main.simulation.HandRank;
import main.simulation.HandResult;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OutcomesDAOIntegrationTest
        extends DAOIntegrationTestSupport {

    @Test
    void save_persistsOutcomeData()
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

        ArrayList<Card> bestFive =
                new ArrayList<>(
                        List.of(
                                new Card(
                                        Card.Rank.KING,
                                        Card.Suit.HEART
                                ),
                                new Card(
                                        Card.Rank.KING,
                                        Card.Suit.CLUB
                                ),
                                new Card(
                                        Card.Rank.ACE,
                                        Card.Suit.CLUB
                                ),
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.SPADE
                                ),
                                new Card(
                                        Card.Rank.NINE,
                                        Card.Suit.HEART
                                )
                        )
                );

        player.setPlayerResults(
                new HandResult(
                        HandRank.PAIR,
                        bestFive,
                        2
                )
        );

        player.setWinner(true);

        OutcomesDAO dao =
                new OutcomesDAO(cn);


        // ACT
        dao.save(
                player,
                simulationId,
                playerId
        );


        // ASSERT
        String sql = """
                SELECT
                    hand_rank,
                    hand_strength,
                    is_winner,
                    best_card_1_rank,
                    best_card_1_suit,
                    best_card_5_rank,
                    best_card_5_suit
                FROM outcomes
                WHERE simulation_player_id = ?
                """;

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(1, playerId);

            try (ResultSet rs =
                         ps.executeQuery()) {

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
                        rs.getString(
                                "best_card_1_rank"
                        )
                );

                assertEquals(
                        "HEART",
                        rs.getString(
                                "best_card_1_suit"
                        )
                );

                assertEquals(
                        "NINE",
                        rs.getString(
                                "best_card_5_rank"
                        )
                );

                assertEquals(
                        "HEART",
                        rs.getString(
                                "best_card_5_suit"
                        )
                );

                assertFalse(rs.next());
            }
        }
    }


    @Test
    void save_rejectsBestFiveWithWrongSize()
            throws SQLException {

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

        ArrayList<Card> onlyFourCards =
                new ArrayList<>(
                        List.of(
                                aceClub,
                                kingHeart,
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.SPADE
                                ),
                                new Card(
                                        Card.Rank.NINE,
                                        Card.Suit.HEART
                                )
                        )
                );

        player.setPlayerResults(
                new HandResult(
                        HandRank.HIGH_CARD,
                        onlyFourCards,
                        1
                )
        );

        OutcomesDAO dao =
                new OutcomesDAO(cn);


        assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(
                        player,
                        simulationId,
                        playerId
                )
        );

        assertEquals(
                0,
                countRows("outcomes")
        );
    }


    @Test
    void save_rejectsMismatchedHandStrength()
            throws SQLException {

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

        ArrayList<Card> fiveCards =
                new ArrayList<>(
                        List.of(
                                aceClub,
                                kingHeart,
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.SPADE
                                ),
                                new Card(
                                        Card.Rank.NINE,
                                        Card.Suit.HEART
                                ),
                                new Card(
                                        Card.Rank.SEVEN,
                                        Card.Suit.DIAMOND
                                )
                        )
                );

        /*
         * Intentionally impossible:
         *
         * PAIR should have strength 2,
         * but we give it strength 9.
         */
        player.setPlayerResults(
                new HandResult(
                        HandRank.PAIR,
                        fiveCards,
                        9
                )
        );

        OutcomesDAO dao =
                new OutcomesDAO(cn);


        assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(
                        player,
                        simulationId,
                        playerId
                )
        );

        assertEquals(
                0,
                countRows("outcomes")
        );
    }
}