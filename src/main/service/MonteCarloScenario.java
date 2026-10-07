package main.service;

import main.model.Card;

import java.util.ArrayList;

/**
 * Define user input
 */
public class MonteCarloScenario {

    private ArrayList<Card> heroCards;
    private ArrayList<Card> knownCommunityCards;
    private int numTrials;
    private int numOpponents;

    public MonteCarloScenario(ArrayList<Card> heroCards, ArrayList<Card> knownCommunityCards, int numTrials, int numOpponents) {
        this.heroCards = heroCards;
        this.knownCommunityCards = knownCommunityCards;
        this.numTrials = numTrials;
        this.numOpponents = numOpponents;
    }

    public ArrayList<Card> getHeroCards() {
        return heroCards;
    }

    public void setHeroCards(ArrayList<Card> heroCards) {
        this.heroCards = heroCards;
    }

    public ArrayList<Card> getKnownCommunityCards() {
        return knownCommunityCards;
    }

    public void setKnownCommunityCards(ArrayList<Card> knownCommunityCards) {
        this.knownCommunityCards = knownCommunityCards;
    }

    public int getNumTrials() {
        return numTrials;
    }

    public void setNumTrials(int numTrials) {
        this.numTrials = numTrials;
    }

    public int getNumOpponents() {
        return numOpponents;
    }

    public void setNumOpponents(int numOpponents) {
        this.numOpponents = numOpponents;
    }
}
