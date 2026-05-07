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
            // TODO assign params thru ps

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create features row");
        }
    }
}
