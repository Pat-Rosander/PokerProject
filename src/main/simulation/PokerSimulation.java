package main.simulation;

import main.model.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokerSimulation extends HandEvaluator {
    private Deck deck;
    private ArrayList<Player> players;
    private ArrayList<Player> winningPlayers;
    private Card[] communityCards;

    // Construct simulation
    public PokerSimulation() {
        deck = new Deck();
        players = new ArrayList<>();
        communityCards = new Card[5];
        winningPlayers = new ArrayList<>();
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

    public void setWinningPlayers(ArrayList<Player> winningPlayers) {
        this.winningPlayers = winningPlayers;
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

    public ArrayList<Player> getWinningPlayers() {
        return winningPlayers;
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

    public void handleWinners() {
        // For each player in game
            // Evaluate hand (Check each HandEvaluator method)
            // Store each players total hand strength (hand strength enum + high card)
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            HandResult currentHandResult = currentPlayer.getPlayerResults();
            ArrayList<Card> currentCardsALl = playerCardAll(currentPlayer.getHoleCards(), communityCards);

            if (isRoyalFlush(currentCardsALl)) {
                currentHandResult.setRank(HandRank.ROYAL_FLUSH);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isStraightFlush(currentCardsALl)) {
                currentHandResult.setRank(HandRank.STRAIGHT_FLUSH);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isFourOfKind(currentCardsALl)) {
                currentHandResult.setRank(HandRank.FOUR_OF_A_KIND);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isFullHouse(currentCardsALl)) {
                currentHandResult.setRank(HandRank.FULL_HOUSE);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isFlush(currentCardsALl)) {
                currentHandResult.setRank(HandRank.FLUSH);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isStraight(currentCardsALl)) {
                currentHandResult.setRank(HandRank.STRAIGHT);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isThreeOfKind(currentCardsALl)) {
                currentHandResult.setRank(HandRank.THREE_OF_A_KIND);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isTwoPair(currentCardsALl)) {
                currentHandResult.setRank(HandRank.TWO_PAIR);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else if (isOnePair(currentCardsALl)) {
                currentHandResult.setRank(HandRank.PAIR);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
            else {
                currentHandResult.setRank(HandRank.HIGH_CARD);
                currentHandResult.setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
                currentHandResult.setBestFiveCards(bestFiveCards);
            }
        }

        Player winningPlayer = comparePlayers();
        winningPlayers.add(winningPlayer);
    }

    private Player comparePlayers() {
        Player highestValuePlayer = new Player();
        int highestValue = 0;

        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            int currValue = currPlayer.getPlayerResults().getHandStrength();

            if (highestValue < currValue) {
                highestValuePlayer = currPlayer;
                highestValue = currValue;
            }
            else if (highestValue == currValue) { // If 2 players have same hand rank
                // Check who has high card

                // if high cards equal each other, then return two players
            }

        }

        return highestValuePlayer;
    }
}