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

    public void handleWinners() {
        // For each player in game
            // Evaluate hand (Check each HandEvaluator method)
            // Store each players total hand strength (hand strength enum + high card)
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            ArrayList<Card> currentCardsALl = playerCardAll(currentPlayer.getHoleCards(), communityCards);

            if (isRoyalFlush(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.ROYAL_FLUSH);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isStraightFlush(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.STRAIGHT_FLUSH);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isFourOfKind(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.FOUR_OF_A_KIND);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isFullHouse(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.FULL_HOUSE);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isFlush(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.FLUSH);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isStraight(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.STRAIGHT);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isThreeOfKind(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.THREE_OF_A_KIND);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isTwoPair(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.TWO_PAIR);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
            else if (isOnePair(currentCardsALl)) {
                currentPlayer.getPlayerResults().setRank(HandRank.PAIR);
                currentPlayer.getPlayerResults().setHandStrength(currentPlayer.getPlayerResults().convertHandRankToNum());
                ArrayList<Card> bestFiveCards = new ArrayList<Card>(currentCardsALl.subList(0, 5));
            }
        }

        // Call comparePlayers()

    }

    public void comparePlayers() {

        // Check which player has highest value hand
        // if two players handValue equal each other
        // evaluate who has high card
        // Assign winning player
    }
}