package main.db;

import main.model.Card;
import main.model.Player;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import main.db.DatabaseManager;

public class PlayerDAO {
    private final Connection connection;

    public PlayerDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Player> get(long id) {
        return Optional.empty();
    }

    public List<Player> getAll() {
        return List.of();
    }

    public long save(Player player) throws SQLException {
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
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Card card1 = player.getHoleCards().get(0);
            Card card2 = player.getHoleCards().get(1);

            //ps.setLong(1, /* simulationId */);
            //ps.setInt(2, /* playerPosition */);
            ps.setString(3, card1.getRank().name());
            ps.setString(4, card1.getSuit().name());
            ps.setString(5, card2.getRank().name());
            ps.setString(6, card2.getSuit().name());

            ps.executeUpdate();

            return -1; // TODO return simulation_player_id

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save player data", e);
        }
    }

    public void update(Player player, String[] params) {

    }

    public void delete(Player player) {

    }
}
