package main.service;

import main.simulation.HandRank;

import java.util.Map;

/**
 * Monte Carlo aggregate output
 */
public class MonteCarloResults {
    private int totalTrials;
    private int numWins;
    private int numLose;
    private int numTies;
    private Map<HandRank, Integer> heroHandRankDistribution;

    public MonteCarloResults(int totalTrials, int numWins, int numLose, int numTies, Map<HandRank, Integer> heroHandRankDistribution) {
        this.totalTrials = totalTrials;
        this.numWins = numWins;
        this.numLose = numLose;
        this.numTies = numTies;
        this.heroHandRankDistribution = heroHandRankDistribution;
    }
}
