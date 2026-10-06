package main.db;

import main.model.*;
import java.sql.*;

public class FeaturesDAO {
    private final Connection connection;

    public FeaturesDAO(Connection connection) {
        this.connection = connection;
    }

    public void save (Player player,
                      long simulationId,
                      long simulationPlayerId,
                      int playerPosition,
                      int playerCount) throws SQLException {
        // Throw exception if player has invalid hole cards state
        if (player.getHoleCards() == null || player.getHoleCards().size() != 2) {
            throw new IllegalArgumentException("Player must have exactly 2 hole cards");
        }

        Card card1 = player.getHoleCards().get(0);
        Card card2 = player.getHoleCards().get(1);

        int rank1 = card1.convertRankToNum();
        int rank2 = card2.convertRankToNum();
        int highCardRank = Math.max(rank1, rank2);
        int lowCardRank = Math.min(rank1, rank2);
        int rankGap = highCardRank - lowCardRank;
        boolean isPair = rankGap == 0;
        boolean isSuited = card1.getSuit() == card2.getSuit();
        boolean isConnected = rankGap == 1;
        String holeCardCategory = getHoleCardCategory(highCardRank, lowCardRank, isPair, isSuited);

        String sql = """
            INSERT INTO features (
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
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, simulationId);
            ps.setLong(2, simulationPlayerId);
            ps.setString(3, holeCardCategory);
            ps.setBoolean(4, isPair);
            ps.setBoolean(5, isSuited);
            ps.setBoolean(6, isConnected);
            ps.setInt(7, rankGap);
            ps.setInt(8, highCardRank);
            ps.setInt(9, lowCardRank);
            ps.setInt(10, playerCount);
            ps.setInt(11, playerPosition);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create features row", e);
        }
    }

    private String getHoleCardCategory(int highCardRank,
                                       int lowCardRank,
                                       boolean isPair,
                                       boolean isSuited) {
        if (isPair) {
            return rankLabel(highCardRank) + rankLabel(lowCardRank);
        }

        String suitedness = isSuited ? "s" : "o";
        return rankLabel(highCardRank) + rankLabel(lowCardRank) + suitedness;
    }

    private String rankLabel(int rank) {
        return switch (rank) {
            case 14 -> "A";
            case 13 -> "K";
            case 12 -> "Q";
            case 11 -> "J";
            case 10 -> "T";
            case 9 -> "9";
            case 8 -> "8";
            case 7 -> "7";
            case 6 -> "6";
            case 5 -> "5";
            case 4 -> "4";
            case 3 -> "3";
            case 2 -> "2";
            default -> throw new IllegalArgumentException("Unknown card rank: " + rank);
        };
    }
}
