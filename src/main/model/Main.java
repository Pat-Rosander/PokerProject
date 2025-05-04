package main.model;

import main.simulation.HandResult;
import main.simulation.PokerSimulation;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        PokerSimulation simulation = new PokerSimulation();

        simulation.setCommunityCards( new Card[] {
            new Card(Card.Rank.KING, Card.Suit.HEART),
            new Card(Card.Rank.SIX, Card.Suit.SPADE),
            new Card(Card.Rank.SEVEN, Card.Suit.CLUB),
            new Card(Card.Rank.EIGHT, Card.Suit.HEART),
            new Card(Card.Rank.KING, Card.Suit.DIAMOND),
        });

        simulation.setPlayers(
                new ArrayList<Player>(Arrays.asList(
                        new Player("player1", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.ACE, Card.Suit.HEART),
                                new Card(Card.Rank.TWO, Card.Suit.SPADE)
                        ))
                        ),
                        new Player("player2", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                                new Card(Card.Rank.TWO, Card.Suit.CLUB)
                        ))
                        )
                ))
        );

        Card[] communityCards = simulation.getCommunityCards();
        System.out.println("table cards: ");
        System.out.println(Arrays.stream(communityCards).toList());
        System.out.println("");

        simulation.handleWinners(); 

        for (int j = 0; j < simulation.getPlayers().size(); j++) {
            Player currPlayer = simulation.getPlayers().get(j);
            System.out.println(currPlayer + " hand results:");
            System.out.println(currPlayer.getPlayerResults());
            System.out.println("");
        }

        System.out.println("Winning player: " + simulation.getWinningPlayers());

    }
}