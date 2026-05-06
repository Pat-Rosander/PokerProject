package main.model;

import main.simulation.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    // Test connection
    public static void main(String[] args) {

        PokerSimulation simulation = new PokerSimulation();
        simulation.runSimulation(2);
        for (int i = 0; i < simulation.getPlayers().size(); i++) {
            System.out.println(simulation.getPlayers().get(i).getHoleCards().toString());
        }

        /*
        System.out.println(simulation.toString());
        for (int i = 0; i < simulation.getWinningPlayers().size(); i++) {
            System.out.println(simulation.getWinningPlayers().get(i).getPlayerResults());
        }

        ArrayList<Card> testHand = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.SIX, Card.Suit.SPADE),
                new Card(Card.Rank.SEVEN, Card.Suit.CLUB),
                new Card(Card.Rank.EIGHT, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND)
        ));
        System.out.println(simulation.sortByAscendingRank(testHand));
        System.out.println(simulation.sortByDescendingRank(testHand));
        */
    }
}