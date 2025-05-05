package test;

import main.model.*;
import main.simulation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck deck = new Deck();
    PokerSimulation simulation = new PokerSimulation();

    @Test
    @DisplayName("Size test after a shuffle")
    void shuffleSize() {
        deck = simulation.getDeck();
        deck.shuffle();
        assertEquals(52, deck.size());
    }

    @Test
    @DisplayName("main.model.Deck contains all cards test")
    void deckContainsAll() {
        deck = simulation.getDeck();
        deck.shuffle();
        assertEquals(true, deck.containsAllCards());
    }

    @Test
    @DisplayName("main.simulation.PokerSimulation playerSetup method")
    void playerSetup() {
        // Add 3 players to simulation
        simulation.addPlayer("player1");
        simulation.addPlayer("player2");
        simulation.addPlayer("player3");
        deck = simulation.getDeck();
        // Each player dealt holeCards upon initialization, deck size should be 46.
        assertEquals(46, deck.size());
        Player player1 = simulation.getPlayers().get(0);
        assertEquals(2, player1.getHoleCards().size());
    }

    @Test
    @DisplayName("Player getHoleCards test")
    void getHoleCards() {
        simulation.addPlayer("player1");
        simulation.addPlayer("player2");
        simulation.addPlayer("player3");

        Player player1 = simulation.getPlayers().get(0);
        assertEquals(2, player1.getHoleCards().size());
    }

    @Test
    @DisplayName("Check that players holeCards are not included in deck")
    void checkIfHoleCardsInDeck() {
        simulation.addPlayer("player1");
        simulation.addPlayer("player2");
        simulation.addPlayer("player3");
        deck = simulation.getDeck();

        for (int i = 0; i < simulation.getPlayers().size(); i++) {
            ArrayList<Card> tempPlayerHoleCard = simulation.getPlayers().get(i).getHoleCards();
            for (int j = 0; j < tempPlayerHoleCard.size(); j++) {
                    assertEquals(false, deck.containsCard(tempPlayerHoleCard.get(j)));
            }
        }
    }

    @Test
    @DisplayName("Check if player has flush")
    void checkFlush() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.NINE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.HEART));

        assertEquals(true, simulation.isFlush(testHand));
    }

    @Test
    @DisplayName("Check if player has straight")
    void checkStraight() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.SIX, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.NINE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.FIVE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.HEART));

        assertEquals(true, simulation.isStraight(testHand));
    }

    @Test
    @DisplayName("Check if player has special case ace straight")
    void checkAceStraight() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.NINE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.FIVE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.HEART));

        assertEquals(true, simulation.isStraight(testHand));
    }

    @Test
    @DisplayName("Check if player has royal flush")
    void checkRoyalFlush() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.QUEEN, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.JACK, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.TEN, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.CLUB));

        assertEquals(true, simulation.isRoyalFlush(testHand));
    }

    @Test
    @DisplayName("Check if player has straight flush")
    void checkStraightFlush() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.FIVE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.EIGHT, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.SIX, Card.Suit.SPADE));

        assertEquals(true, simulation.isStraightFlush(testHand));
    }

    @Test
    @DisplayName("Check is player has four of a kind")
    void checkFourOfKind() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.JACK, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.DIAMOND));

        assertEquals(true, simulation.isFourOfKind(testHand));
    }

    @Test
    @DisplayName("Check if player has three of a kind")
    void checkThreeOfKind() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.TEN, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.SPADE));

        assertEquals(true, simulation.isThreeOfKind(testHand));
    }

    @Test
    @DisplayName("Check if player has full house")
    void checkFullHouse() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.JACK, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.FIVE, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.JACK, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.SEVEN, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.JACK, Card.Suit.CLUB));

        assertEquals(true, simulation.isFullHouse(testHand));
    }

    @Test
    @DisplayName("Check if player has two pair")
    void checkTwoPair() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.EIGHT, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.SPADE));

        assertEquals(true, simulation.isTwoPair(testHand));
    }

    @Test
    @DisplayName("Check if player has one pair")
    void checkOnePair() {
        ArrayList<Card> testHand = new ArrayList<Card>();
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMOND));
        testHand.add(new Card(Card.Rank.THREE, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.SPADE));
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.CLUB));
        testHand.add(new Card(Card.Rank.FOUR, Card.Suit.HEART));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.SPADE));

        assertEquals(true, simulation.isOnePair(testHand));
    }

    @Test
    @DisplayName("Test tie breaker case between two players with same value hand")
    void checkTieBreakerBetweenTwoPlayers() {
        // Add two players to simulation with exact same value holeCards
        // p1 --> Ace of Hearts and Two of Spades
        // p2 --> Ace of Spades and Two of Clubs
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
        // Cards on table for tie-breaker case
        simulation.setCommunityCards( new Card[] {
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.SIX, Card.Suit.SPADE),
                new Card(Card.Rank.SEVEN, Card.Suit.CLUB),
                new Card(Card.Rank.EIGHT, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND),
        });
        simulation.handleWinners();
        // Two players with same value hand, and same value high card; therefore must be a tie
        assertEquals(2, simulation.getWinningPlayers().size());
    }
    @Test
    @DisplayName("Test high card between two players with same value hand")
    void checkHighCardBetweenTwoPlayers() {
        // Add two players to simulation with exact same value holeCards
        // p1 --> Ace of Hearts and Two of Spades
        // p2 --> Queen of Spades and Two of Clubs
        simulation.setPlayers(
                new ArrayList<Player>(Arrays.asList(
                        new Player("player1", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.ACE, Card.Suit.HEART),
                                new Card(Card.Rank.TWO, Card.Suit.SPADE)
                        ))
                        ),
                        new Player("player2", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.QUEEN, Card.Suit.SPADE),
                                new Card(Card.Rank.TWO, Card.Suit.CLUB)
                        ))
                        )
                ))
        );
        // Cards on table for tie-breaker case
        simulation.setCommunityCards( new Card[] {
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.SIX, Card.Suit.SPADE),
                new Card(Card.Rank.SEVEN, Card.Suit.CLUB),
                new Card(Card.Rank.EIGHT, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND),
        });
        simulation.handleWinners();

        ArrayList<Card> actualWinningHand = simulation.getWinningPlayers().get(0).getPlayerResults().getBestFiveCards();
        ArrayList<Card> winningHand = new ArrayList<Card>();
        winningHand.add(new Card(Card.Rank.ACE, Card.Suit.HEART));
        winningHand.add(new Card(Card.Rank.KING, Card.Suit.HEART));
        winningHand.add(new Card(Card.Rank.KING, Card.Suit.DIAMOND));
        winningHand.add(new Card(Card.Rank.EIGHT, Card.Suit.HEART));
        winningHand.add(new Card(Card.Rank.SEVEN, Card.Suit.CLUB));
        simulation.sortByRank(winningHand);

        assertAll("High card comparison",
                () -> assertEquals(winningHand, actualWinningHand),
                () -> assertEquals(1, simulation.getWinningPlayers().size())
        );
    }
}