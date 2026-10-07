package test.integration;

import main.db.CommunityCardsDAO;
import main.model.Card;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommunityCardsDAOIntegrationTest
        extends DAOIntegrationTestSupport {

    @Test
    void save_persistsFiveCommunityCards()
            throws SQLException {

        // ARRANGE
        long simulationId =
                insertTestSimulation(2);

        ArrayList<Card> board =
                new ArrayList<>(
                        List.of(
                                new Card(
                                        Card.Rank.TWO,
                                        Card.Suit.CLUB
                                ),
                                new Card(
                                        Card.Rank.SEVEN,
                                        Card.Suit.DIAMOND
                                ),
                                new Card(
                                        Card.Rank.NINE,
                                        Card.Suit.HEART
                                ),
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.SPADE
                                ),
                                new Card(
                                        Card.Rank.KING,
                                        Card.Suit.CLUB
                                )
                        )
                );

        CommunityCardsDAO dao =
                new CommunityCardsDAO(cn);


        // ACT
        dao.save(
                board,
                simulationId
        );


        // ASSERT
        String sql = """
                SELECT *
                FROM community_cards
                WHERE simulation_id = ?
                """;

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(1, simulationId);

            try (ResultSet rs =
                         ps.executeQuery()) {

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
    }


    @Test
    void save_rejectsBoardWithoutExactlyFiveCards()
            throws SQLException {

        long simulationId =
                insertTestSimulation(2);

        ArrayList<Card> invalidBoard =
                new ArrayList<>(
                        List.of(
                                new Card(
                                        Card.Rank.TWO,
                                        Card.Suit.CLUB
                                ),
                                new Card(
                                        Card.Rank.SEVEN,
                                        Card.Suit.DIAMOND
                                ),
                                new Card(
                                        Card.Rank.NINE,
                                        Card.Suit.HEART
                                ),
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.SPADE
                                )
                        )
                );

        CommunityCardsDAO dao =
                new CommunityCardsDAO(cn);


        assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(
                        invalidBoard,
                        simulationId
                )
        );

        assertEquals(
                0,
                countRows("community_cards")
        );
    }


    @Test
    void save_rejectsNonexistentSimulationForeignKey() {

        ArrayList<Card> board =
                new ArrayList<>(
                        List.of(
                                new Card(
                                        Card.Rank.TWO,
                                        Card.Suit.CLUB
                                ),
                                new Card(
                                        Card.Rank.SEVEN,
                                        Card.Suit.DIAMOND
                                ),
                                new Card(
                                        Card.Rank.NINE,
                                        Card.Suit.HEART
                                ),
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.SPADE
                                ),
                                new Card(
                                        Card.Rank.KING,
                                        Card.Suit.CLUB
                                )
                        )
                );

        CommunityCardsDAO dao =
                new CommunityCardsDAO(cn);


        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dao.save(
                                board,
                                -999
                        )
                );

        assertInstanceOf(
                SQLException.class,
                exception.getCause()
        );
    }
}