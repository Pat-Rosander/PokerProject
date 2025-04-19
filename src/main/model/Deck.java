package main.model;

import main.model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>(); // main.model.Deck has a list of 52 cards
    // Constructor
    public Deck() {
        reset();
        shuffle();
    }
    public void reset() {
        this.cards.clear();
        cards = new ArrayList<Card>();
        for(Card.Suit suit : Card.Suit.values()) {
            for(Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }
    public ArrayList<Card> shuffle() {
        Random rand = new Random();

        for (int i = 0; i < cards.size(); i++) {
            int cardIndex = rand.nextInt(i, cards.size()); // Assign cardIndex with random int
            Card currentCard = cards.get(cardIndex); // Assign currentCard with cardIndex
            Card temp;
            temp = cards.get(i);

            cards.set(i, currentCard); // set i to currentCard
            cards.set(cardIndex, temp); // set cardIndex to temp
        }
        return cards;
    }
    //Remove next card from deck
    public Card getNextCard() {
        return this.cards.remove(this.cards.size() - 1);
    }
    public Boolean containsCard(Card card) {
        for (int i = 0; i < cards.size(); i++) {
            if (!cards.contains(card)) {
                return false;
            }
        }
        return true;
    }
    public Boolean containsAllCards() {
        if (cards.size() != 52) { // Check deck size
            return false;
        }
        for (Card.Suit suit : Card.Suit.values()) { // Iterate deck for each rank and suit
            for (Card.Rank rank : Card.Rank.values()) {
                Card expectedCard = new Card(rank, suit);
                if (!cards.contains(expectedCard)) {
                    return false;
                }
            }
        }
        return true;
    }
    public void print() {
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(cards.get(i).toString() + " ");
        }
    }
    public int size() {
        return cards.size();
    }
}
