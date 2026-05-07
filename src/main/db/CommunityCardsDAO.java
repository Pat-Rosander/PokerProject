package main.db;

import main.model.Card;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class CommunityCardsDAO {
    private final Connection connection;

    public CommunityCardsDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Card> get(long id) {
        return Optional.empty();
    }

    public List<Card> getAll() {
        return List.of();
    }

    public void save(ArrayList<Card> communityCards, long simulationId) {
        if (communityCards.size() != 5) {
            throw new IllegalArgumentException("Community cards must be 5 cards");
        }
        String sql = """
            INSERT INTO community_cards (
                simulation_id,
                flop_1_rank,
                flop_1_suit,
                flop_2_rank,
                flop_2_suit,
                flop_3_rank,
                flop_3_suit,
                turn_rank,
                turn_suit,
                river_rank,
                river_suit
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Card c1 = communityCards.get(0);
            Card c2 = communityCards.get(1);
            Card c3 = communityCards.get(2);
            Card c4 = communityCards.get(3);
            Card c5 = communityCards.get(4);

            ps.setLong(1, simulationId);

            int i = 2;
            for (Card card : communityCards) {
                ps.setString(i++, card.getRank().name());
                ps.setString(i++, card.getSuit().name());
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create community_cards row", e);
        }
    }

    public void update (Card card, String[] params) {

    }

    public void delete (Card card) {

    }
}
