package main.simulation;
import main.model.*;

import java.util.ArrayList;

public class HandResult {
    private HandRank rank;
    private ArrayList<Card> bestFiveCards;
    private int handStrength;

    public HandResult(HandRank rank, ArrayList<Card> bestFiveCards, int handStrength) {
        this.rank = rank;
        this.bestFiveCards = bestFiveCards;
        this.handStrength = handStrength;
    }
    public void setRank(HandRank rank) {
        this.rank = rank;
    }
    public void setBestFiveCards(ArrayList<Card> bestFiveCards) {
        this.bestFiveCards = bestFiveCards;
    }
    public void setHandStrength(int handStrength) {
        this.handStrength = handStrength;
    }
    public HandRank getRank() {
        return rank;
    }
    public ArrayList<Card> getBestFiveCards() {
        return bestFiveCards;
    }
    public int getHandStrength() {
        return handStrength;
    }
    public int convertHandRankToNum() {
        switch (this.rank) {
            case HIGH_CARD:
                return 1;
            case PAIR:
                return 2;
            case TWO_PAIR:
                return 3;
            case THREE_OF_A_KIND:
                return 4;
            case STRAIGHT:
                return 5;
            case FLUSH:
                return 6;
            case FULL_HOUSE:
                return 7;
            case FOUR_OF_A_KIND:
                return 8;
            case STRAIGHT_FLUSH:
                return 9;
            case ROYAL_FLUSH:
                return 10;
            default:
                return -1;
        }
    }

    @Override
    public String toString() {
        return bestFiveCards + " is a " + rank + "(" + handStrength + ")";
    }
}