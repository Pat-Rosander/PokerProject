package main.service;

import main.simulation.HandRank;

import java.util.Map;

/**
 * Monte Carlo aggregate output
 */
public class MonteCarloResults {
    private final int totalTrials;
    private final int numWins;
    private final int numLosses;
    private final int numTies;
    private final Map<HandRank, Integer> heroHandRankDistribution;

    public MonteCarloResults(int totalTrials, int numWins, int numLosses, int numTies, Map<HandRank, Integer> heroHandRankDistribution) {
        this.totalTrials = totalTrials;
        this.numWins = numWins;
        this.numLosses = numLosses;
        this.numTies = numTies;
        this.heroHandRankDistribution = Map.copyOf(heroHandRankDistribution);
    }

    public int getTotalTrials() {
        return totalTrials;
    }

    public int getNumWins() {
        return numWins;
    }

    public int getNumLosses() {
        return numLosses;
    }

    public int getNumTies() {
        return numTies;
    }

    public Map<HandRank, Integer> getHeroHandRankDistribution() {
        return heroHandRankDistribution;
    }
}
