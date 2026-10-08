package main.service;

import main.model.Card;
import main.simulation.HandRank;
import main.simulation.PokerSimulation;
import main.simulation.SimulationResults;

import java.util.*;

/**
 * MonteCarloSimulationService
 *       ↓
 * run PokerSimulation repeatedly
 *       ↓
 * aggregate those individual results
 *       ↓
 * MonteCarloResults
 *        ↓
 * return to user or persistence layer
 */
public class MonteCarloSimulationService {

    public MonteCarloResults runSimulation(MonteCarloScenario scenario) {

        int numWins = 0;
        int numLose = 0;
        int numTies = 0;

        Map<HandRank, Integer> heroHandRankDistribution = new EnumMap<>(HandRank.class);

        validateMonteCarloScenario(scenario);

        for (int trial = 0; trial < scenario.getNumTrials(); trial++) {

            PokerSimulation simulation = new PokerSimulation();

            // Configure known state
            // Add fixed hero

            simulation.addPlayer(
                    "hero",
                    scenario.getHeroCards()
                    );

            // Set known board

            simulation.setKnownCommunityCards(scenario.getKnownCommunityCards());

            // Remove all known cards before adding random opponents

            for (int i = 1; i < scenario.getNumOpponents(); i++) {
                simulation.addRandomPlayer(
                        "opponent" + i
                );
            }

            SimulationResults trialResults = simulation.runSimulation();

            // TODO
            // inspect hero
            // update win/tie/loss
            // update HandRank distribution
        }

        return new MonteCarloResults(
            scenario.getNumTrials(),
                numWins,
                numLose,
                numTies,
                heroHandRankDistribution
        );
    }

    private void validateMonteCarloScenario(MonteCarloScenario scenario) {
        List<Card> scenarioCommunityCards = scenario.getKnownCommunityCards();

        int boardSize = scenarioCommunityCards.size();

        if (scenario == null) {
            throw new IllegalArgumentException(
                    "Monte Carlo scenario cannot be null"
            );
        }

        if (scenario.getNumTrials() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid scenario - number of trials must be greater than 0"
            );
        }

        if (boardSize != 0 &&
                boardSize != 3 &&
                boardSize != 4 &&
                boardSize != 5) {
            throw new IllegalArgumentException("Invalid scenario - community cards must be size 0, 3, 4 or 5");
        }

        if (scenario.getNumOpponents() < 1 ||
                scenario.getNumOpponents() > 9) {
            throw new IllegalArgumentException("Invalid scenario - opponents must be between 1 and 9");
        }

        if (scenario.getHeroCards().size() != 2) {
            throw new IllegalArgumentException("Invalid scenario - number of hero cards must be 2");
        }

        Set<Card> knownCards = new HashSet<>();

        for (Card card : scenario.getHeroCards()) {
            if (!knownCards.add(card)) {
                throw new IllegalArgumentException("Duplicate card");
            }
        }

        for (Card card : scenario.getKnownCommunityCards()) {
            if (!knownCards.add(card)) {
                throw new IllegalArgumentException("Duplicate card");
            }
        }
    }
}