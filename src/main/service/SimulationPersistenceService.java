package main.service;

import main.db.CommunityCardsDAO;
import main.db.PlayerDAO;
import main.db.SimulationDAO;
import main.db.DatabaseManager;
import main.simulation.SimulationResults;
import main.model.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SimulationPersistenceService {
    private final SimulationDAO simulationDAO;
    private final PlayerDAO playerDAO;
    private final CommunityCardsDAO communityCardsDAO;

    public SimulationPersistenceService() throws SQLException {
        Connection connection = DatabaseManager.getConnection();

        this.simulationDAO = new SimulationDAO(connection);
        this.playerDAO = new PlayerDAO(connection);
        this.communityCardsDAO = new CommunityCardsDAO(connection);
    }

    public void saveSimulationResults(SimulationResults simulationResults) throws SQLException {
        long simulationId = simulationDAO.save(simulationResults);

        for (int i = 0; i < simulationResults.getPlayersList().size(); i++) {
            Player player = simulationResults.getPlayersList().get(i);
            int playerPosition = i + 1;

            long simulationPlayerId = playerDAO.save(player, simulationId, playerPosition);
            // TODO save OutcomesDAO and FeaturesDAO
        }

        communityCardsDAO.save(simulationResults.getCommunityCards(), simulationId);
    }
}
