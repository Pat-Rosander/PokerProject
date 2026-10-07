package main.db;

import main.model.*;
import main.simulation.HandResult;

import java.sql.*;

public class OutcomesDAO {
    private final Connection connection;

    public OutcomesDAO(Connection connection) {
       this.connection = connection;
    }

    public void save (Player player, long simulationId, long simulationPlayerId) {

        HandResult result  = player.getPlayerResults();

        if (result == null) {
            throw new IllegalArgumentException(
                    "Player must have a hand result"
            );
        }

        if (result.getRank() == null) {
            throw new IllegalArgumentException(
                    "Hand result must have a rank"
            );
        }

        if (result.getBestFiveCards() == null ||
                result.getBestFiveCards().size() != 5) {
            throw new IllegalArgumentException(
                    "Hand result must contain exactly 5 best cards"
            );
        }

        if (result.getHandStrength() != result.convertHandRankToNum()) {
            throw new IllegalArgumentException(
                    "Hand strength does not match hand rank"
            );
        }

        for (Card card : result.getBestFiveCards()) {

            if (card == null || card.getRank() == null || card.getSuit() == null) {
                throw new IllegalArgumentException(
                        "Card must not be null."
                );
            }
        }

        String sql = """
            INSERT INTO outcomes (
                simulation_id,
                simulation_player_id,
                hand_rank,
                hand_strength,
                is_winner,
                best_card_1_rank,
                best_card_1_suit,
                best_card_2_rank,
                best_card_2_suit,
                best_card_3_rank,
                best_card_3_suit,
                best_card_4_rank,
                best_card_4_suit,
                best_card_5_rank,
                best_card_5_suit
            )
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, simulationId);
            ps.setLong(2, simulationPlayerId);
            ps.setString(3, result.getRank().name());
            ps.setInt(4, result.getHandStrength());
            ps.setBoolean(5, player.getWinner());

            int i = 6;

            for (Card card : result.getBestFiveCards()) {
                ps.setString(i++, card.getRank().name());
                ps.setString(i++, card.getSuit().name());
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create outcome row", e);
        }
    }
}
