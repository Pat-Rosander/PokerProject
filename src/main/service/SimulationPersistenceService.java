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
        Exception originalException = null;

        try {
            // Set auto commit as false to allow rollbacks if necessary
            connection.setAutoCommit(false);

            // simulationDAO save first to create simulationId passed to all other DAOs
            long simulationId = simulationDAO.save(simulationResults);
            int playerCount = simulationResults.getPlayersList().size();

            // For each player
            for (int i = 0; i < playerCount; i++) {
                Player player = simulationResults.getPlayersList().get(i);
                int playerPosition = i + 1;

                // Save rows to player, outcomes, and features tables
                long simulationPlayerId = playerDAO.save(player, simulationId, playerPosition);
                outcomesDAO.save(player, simulationId, simulationPlayerId);
                featuresDAO.save(player, simulationId, simulationPlayerId, playerPosition, playerCount);
            }

            communityCardsDAO.save(simulationResults.getCommunityCards(), simulationId);

            // Explicitly call connection.commit() once all DAO classes have successfully saved rows
            connection.commit();

        // If any tables throw an SQL Exception, then catch, rollback, and setAutoCommit back to true
        } catch (SQLException | RuntimeException e) {
            originalException = e;

            rollback(e);
            throw e;
        } finally {

            // Nested try-catch blocks to prevent an SQL Exception from obscuring rollback logic
            try {
                connection.setAutoCommit(originalAutoCommit);
            }
            catch (SQLException autoCommitException) {

                // If an originalException exists, then attach autoCommitException
                if (originalException != null) {
                    originalException.addSuppressed(autoCommitException);
                } else {
                    throw autoCommitException;
                }
            }
        }
    }

    /**
     * Undo all exchanges made in current transaction
     * @param originalException
     */
    private void rollback(Exception originalException) {
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            originalException.addSuppressed(rollbackException);
        }
    }
}
