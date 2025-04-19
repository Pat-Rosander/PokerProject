package main.simulation;

import main.model.Player;
import main.model.Deck;
import main.model.Card;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokerSimulation extends HandEvaluator {
    private Deck deck;
    private ArrayList<Player> players;
    private Player winningPlayer;
    private Card[] communityCards;

    // Construct simulation
    public PokerSimulation() {
        deck = new Deck();
        players = new ArrayList<>();
        communityCards = new Card[5];
    }

    // Setters
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setCommunityCards(Card[] communityCards) {
        this.communityCards = communityCards;
    }

    // Getters
    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Card[] getCommunityCards() {
        return communityCards;
    }

    // Initialize players in simulation and deal each player holeCards
    public void playerSetup() {
        Scanner scnr = new Scanner(System.in);
        int numPlayers = 0;
        System.out.println("Please enter the number of players.");
        do {
            numPlayers = scnr.nextInt();
            // Determine number of players in game
            switch (numPlayers) {
                case 0:
                    System.out.println("Enter valid number of players.");
                    break;
                case 1:
                    System.out.println("You need friends. Enter valid number of players.");
                    break;
            }
        } while (numPlayers == 0 || numPlayers == 1);

        for (int i = 1; i <= numPlayers; i++) {
            String name = "player" + i;
            System.out.println("Adding " + name);
            addPlayer(name);
        }
        System.out.println(players);
    }

    public void addPlayer(String name) {
        String tempName = name;
        ArrayList<Card> tempHoleCards = new ArrayList<>(Arrays.asList(this.deck.getNextCard(), this.deck.getNextCard()));
        players.add(new Player(tempName, tempHoleCards));
    }

    // Deal community cards
    public void dealNextCommunityCard() {
        if (this.communityCards[0] == null) { //The flop
            this.communityCards[0] = this.deck.getNextCard();
            this.communityCards[1] = this.deck.getNextCard();
            this.communityCards[2] = this.deck.getNextCard();
        } else if (this.communityCards[3] == null) { //The turn
            this.communityCards[3] = this.deck.getNextCard();
        } else if (this.communityCards[4] == null) { //The river
            this.communityCards[4] = this.deck.getNextCard();
        }
    }

    public ArrayList<Integer> getValuesOfHand(ArrayList<Card> holeCards, Card[] communityCards) {
        ArrayList<Integer> resultHand = new ArrayList<>();
        List<Card> tempCardList = Arrays.asList(communityCards);
        System.out.println(tempCardList);

        for (int i = 0; i < holeCards.size(); i++) {
            if (holeCards.get(i) != null) {
                resultHand.add(i, holeCards.get(i).convertRankToNum());
            }
            System.out.println(resultHand);
        }
        for (int j = 0; j < tempCardList.size(); j++) {
            if (tempCardList.get(j) != null) {
                resultHand.add(j + holeCards.size(), tempCardList.get(j).convertRankToNum());
            }
            System.out.println(resultHand);
        }

        return resultHand;
    }
}