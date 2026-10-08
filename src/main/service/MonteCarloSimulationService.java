package main.service;

import main.model.Card;
import main.model.Player;
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
        int numLosses = 0;
        int numTies = 0;

        Map<HandRank, Integer> heroHandRankDistribution = new EnumMap<>(HandRank.class);
         // Initialize every rank to 0
        for (HandRank rank : HandRank.values()) {
            heroHandRankDistribution.put(rank, 0);
        }

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

            for (int i = 1; i <= scenario.getNumOpponents(); i++) {
                simulation.addRandomPlayer(
                        "opponent" + i
                );
            }

            SimulationResults trialResults = simulation.runSimulation();

            // Simulation hero

            Player hero = trialResults.getPlayersList().get(0);

            // Validate hero hole cards

            if (!hero.getHoleCards().equals(scenario.getHeroCards())) {
                throw new IllegalStateException("Simulation hero cards don't match scenario hero cards");
            }

            // Did hero win, lose, or tie?

            ArrayList<Player> resultsWinningPlayers = new ArrayList<>(trialResults.getWinningPlayers());
            int winningPlayersCount = resultsWinningPlayers.size();

            if (resultsWinningPlayers.isEmpty()) {
                throw new IllegalStateException("Winning players empty");
            }

            if (resultsWinningPlayers.contains(hero)) {
                if (resultsWinningPlayers.size() == 1) {
                    numWins++;
                } else {
                    numTies++;
                }
            } else {
                numLosses++;
            }

            // Update hand rank distribution

            HandRank heroRank = hero.getPlayerResults().getRank();

            heroHandRankDistribution.merge(
                    heroRank,
                    1,
                    Integer::sum // equivalent to: (existingValue, newValue) -> existingValue + newValue
                    );
        }

        int distributedTrials =
                heroHandRankDistribution.values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .sum();

        if (distributedTrials != scenario.getNumTrials()) {
            throw new IllegalStateException(
                    "Hand rank distribution does not match trial count"
            );
        }

        int classifiedTrials =
                numWins + numLosses + numTies;

        if (classifiedTrials != scenario.getNumTrials()) {
            throw new IllegalStateException(
                    "Not every Monte Carlo trial was classified"
            );
        }

        return new MonteCarloResults(
            scenario.getNumTrials(),
                numWins,
                numLosses,
                numTies,
                heroHandRankDistribution
        );
    }

    private void validateMonteCarloScenario(MonteCarloScenario scenario) {
        if (scenario == null) {
            throw new IllegalArgumentException(
                    "Monte Carlo scenario cannot be null"
            );
        }

        List<Card> scenarioCommunityCards = scenario.getKnownCommunityCards();

        int boardSize = scenarioCommunityCards.size();

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