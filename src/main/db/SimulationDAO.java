package main.db;

import main.simulation.SimulationResults;

import java.sql.*;
import java.util.Optional;
import java.util.List;

public class SimulationDAO {
    private final Connection connection;

    public SimulationDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<SimulationResults> get(long id) {
        return Optional.empty();
    }

    public List<SimulationResults> getAll() {
        return List.of();
    }

    public long save(SimulationResults simulationResults) throws SQLException {
        String sql = """
            INSERT INTO simulations (
                num_players,
                simulation_type
            )
            VALUES (?, ?)
            RETURNING simulation_id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int num_players = simulationResults.getPlayersList().size();
            ps.setInt(1, num_players);
            ps.setString(2, "MONTE CARLO");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("simulation_id");
            }

            throw new SQLException("No simulation_id returned");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create simulation row", e);
        }
    }

    public void update(SimulationResults simulationResults, String[] params) {

    }

    public void delete(SimulationResults simulationResults) {

    }
}
