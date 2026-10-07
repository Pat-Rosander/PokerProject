package main.service;

import main.model.Card;
import main.simulation.HandRank;
import main.simulation.PokerSimulation;
import main.simulation.SimulationResults;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

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

        int totalTrials;
        int numWins = 0;
        int numLose = 0;
        int numTies = 0;
        Map<HandRank, Integer> heroHandRankDistribution = new EnumMap<>(HandRank.class);

        validateMonteCarloScenario(scenario);

        for (totalTrials = 0; totalTrials < scenario.getNumTrials(); totalTrials++) {


        }



        return new MonteCarloResults(
            totalTrials,
                numWins,
                numLose,
                numTies,
                heroHandRankDistribution
        );
    }

    private void validateMonteCarloScenario(MonteCarloScenario scenario) {
        ArrayList<Card> scenarioCommunityCards = scenario.getKnownCommunityCards();

        if (scenarioCommunityCards.size() != 0 &&
                scenarioCommunityCards.size() != 3 &&
                scenario.getKnownCommunityCards().size() != 4 &&
                scenario.getKnownCommunityCards().size() != 5) {
            throw new IllegalArgumentException("Invalid scenario - community cards must be size 3, 4 or 5");
        }

        for (Card card : scenarioCommunityCards) {

            // TODO
            //  if there are duplicate community card, then throw illegal argument exception

        }

        if (scenario.getNumOpponents() == 0) {
            throw new IllegalArgumentException("Invalid scenario - number of opponents can't be 0");
        }

        if (scenario.getHeroCards().size() != 2) {
            throw new IllegalArgumentException("Invalid scenario - number of hero cards must be 2");
        }

        if (scenario.getHeroCards().get(0) == scenario.getHeroCards().get(1)) {
            throw new IllegalArgumentException("Invalid scenario - hero cards must be unique");
        }
    }
}