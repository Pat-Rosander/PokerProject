package main.model;

public class Card {
    private Rank rank;
    private Suit suit;
    //Constructor
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
    public int convertRankToNum() {
        switch(this.rank) {
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
                return 10;
            case JACK:
                return 11;
            case QUEEN:
                return 12;
            case KING:
                return 13;
            case ACE:
                return 14;
            default:
                return -1;
        }
    }
    public int convertSuitToNum() {
        switch(this.suit) {
            case CLUB:
                return 1;
            case SPADE:
                return 2;
            case HEART:
                return 3;
            case DIAMOND:
                return 4;
            default:
                return -1;
        }
    }
    //Setter methods
    public void setRank(Rank rank) {
        this.rank = rank;
    }
    public void setSuit(Suit suit) {
        this.suit = suit;
    }
    //Getter methods
    public Rank getRank() {
        return rank;
    }
    public Suit getSuit() {
        return suit;
    }
    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object obj) {
        // Check if the objects are the same reference
        if (this == obj) {
            return true;
        }
        // Check if the object is an instance of main.model.Card
        if (!(obj instanceof Card)) {
            return false;
        }
        // Cast the object to a main.model.Card to compare the properties
        Card other = (Card) obj;

        // Two cards are equal if they have the same rank and suit
        return this.rank == other.rank && this.suit == other.suit;
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(rank, suit);
    }
    public enum Suit {
        CLUB, SPADE, HEART, DIAMOND;
    }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
    }
}
