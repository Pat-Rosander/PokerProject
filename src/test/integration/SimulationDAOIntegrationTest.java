package test.integration;

import main.db.SimulationDAO;
import main.model.Player;
import main.simulation.SimulationResults;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationDAOIntegrationTest
        extends DAOIntegrationTestSupport {

    @Test
    void save_persistsSimulationAndReturnsGeneratedId()
            throws SQLException {

        // ARRANGE
        SimulationDAO dao =
                new SimulationDAO(cn);

        ArrayList<Player> players =
                new ArrayList<>(
                        List.of(
                                new Player(),
                                new Player()
                        )
                );

        SimulationResults results =
                new SimulationResults(
                        players,
                        new ArrayList<>(),
                        new ArrayList<>()
                );


        // ACT
        long simulationId =
                dao.save(results);


        // ASSERT
        assertTrue(simulationId > 0);

        String sql = """
                SELECT
                    num_players,
                    simulation_type
                FROM simulations
                WHERE simulation_id = ?
                """;

        try (PreparedStatement ps =
                     cn.prepareStatement(sql)) {

            ps.setLong(1, simulationId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                assertTrue(rs.next());

                assertEquals(
                        2,
                        rs.getInt("num_players")
                );

                assertEquals(
                        "MONTE CARLO",
                        rs.getString(
                                "simulation_type"
                        )
                );

                assertFalse(rs.next());
            }
        }
    }
}