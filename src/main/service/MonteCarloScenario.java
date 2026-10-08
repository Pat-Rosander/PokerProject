package main.service;

import main.model.Card;

import java.util.List;

/**
 * Define user input
 */
public class MonteCarloScenario {

    private final List<Card> heroCards;
    private final List<Card> knownCommunityCards;
    private final int numTrials;
    private final int numOpponents;

    public MonteCarloScenario(List<Card> heroCards, List<Card> knownCommunityCards, int numTrials, int numOpponents) {
        this.heroCards = List.copyOf(heroCards);
        this.knownCommunityCards = List.copyOf(knownCommunityCards);
        this.numTrials = numTrials;
        this.numOpponents = numOpponents;
    }

    public List<Card> getHeroCards() {
        return heroCards;
    }

    public List<Card> getKnownCommunityCards() {
        return knownCommunityCards;
    }

    public int getNumTrials() {
        return numTrials;
    }

    public int getNumOpponents() {
        return numOpponents;
    }
}
