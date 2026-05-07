package main.service;

import main.db.CommunityCardsDAO;
import main.db.FeaturesDAO;
import main.db.OutcomesDAO;
import main.db.PlayerDAO;
import main.db.SimulationDAO;
import main.db.DatabaseManager;
import main.simulation.SimulationResults;
import main.model.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SimulationPersistenceService {
    private final Connection connection;
    private final SimulationDAO simulationDAO;
    private final PlayerDAO playerDAO;
    private final CommunityCardsDAO communityCardsDAO;
    private final OutcomesDAO outcomesDAO;
    private final FeaturesDAO featuresDAO;

    public SimulationPersistenceService() throws SQLException {
        this.connection = DatabaseManager.getConnection();

        this.simulationDAO = new SimulationDAO(connection);
        this.playerDAO = new PlayerDAO(connection);
        this.communityCardsDAO = new CommunityCardsDAO(connection);
        this.outcomesDAO = new OutcomesDAO(connection);
        this.featuresDAO = new FeaturesDAO(connection);
    }

    public void saveSimulationResults(SimulationResults simulationResults) throws SQLException {
        boolean originalAutoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            long simulationId = simulationDAO.save(simulationResults);
            int playerCount = simulationResults.getPlayersList().size();

            for (int i = 0; i < playerCount; i++) {
                Player player = simulationResults.getPlayersList().get(i);
                int playerPosition = i + 1;

                long simulationPlayerId = playerDAO.save(player, simulationId, playerPosition);
                outcomesDAO.save(player, simulationId, simulationPlayerId);
                featuresDAO.save(player, simulationId, simulationPlayerId, playerPosition, playerCount);
            }

            communityCardsDAO.save(simulationResults.getCommunityCards(), simulationId);
            connection.commit();
        } catch (SQLException | RuntimeException e) {
            rollback(e);
            throw e;
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    private void rollback(Exception originalException) {
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
