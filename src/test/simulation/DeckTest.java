package test.simulation;

import main.model.*;
import main.simulation.PokerSimulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        simulation.addRandomPlayer("player1");
        simulation.addRandomPlayer("player2");
        simulation.addRandomPlayer("player3");
        deck = simulation.getDeck();

        assertEquals(46, deck.size());
        Player player1 = simulation.getPlayers().get(0);
        assertEquals(2, player1.getHoleCards().size());
    }

    @Test
    @DisplayName("Player getHoleCards test")
    void getHoleCards() {
        simulation.addRandomPlayer("player1");
        simulation.addRandomPlayer("player2");
        simulation.addRandomPlayer("player3");

        Player player1 = simulation.getPlayers().get(0);
        assertEquals(2, player1.getHoleCards().size());
    }

    @Test
    @DisplayName("Check that players holeCards are not included in deck")
    void checkIfHoleCardsInDeck() {
        simulation.addRandomPlayer("player1");
        simulation.addRandomPlayer("player2");
        simulation.addRandomPlayer("player3");
        deck = simulation.getDeck();

        for (int i = 0; i < simulation.getPlayers().size(); i++) {
            ArrayList<Card> tempPlayerHoleCard = simulation.getPlayers().get(i).getHoleCards();
            for (int j = 0; j < tempPlayerHoleCard.size(); j++) {
                assertEquals(false, deck.containsCard(tempPlayerHoleCard.get(j)));
            }
        }
    }
}
