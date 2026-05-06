package main.simulation;

import main.model.*;
import java.util.Arrays;
import java.util.ArrayList;

public class PokerSimulation extends HandEvaluator {
    private Deck deck;
    private ArrayList<Player> players;
    private ArrayList<Player> winningPlayers;
    private ArrayList<Card> communityCards;
    private SimulationResults results;

    // Construct simulation
    public PokerSimulation() {
        deck = new Deck();
        players = new ArrayList<>();
        communityCards = new ArrayList<>(5);
        winningPlayers = new ArrayList<>();
    }

    public SimulationResults runSimulation(int numPlayers) {
        for (int i = 1; i <= numPlayers; i++) {
            addPlayer("player" + i);
        }
        dealNextCommunityCard();
        handleWinners();
        results = new SimulationResults(players, winningPlayers, communityCards);

        return results;
    }

    // Setters
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setCommunityCards(ArrayList<Card> communityCards) {
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

    public ArrayList<Card> getCommunityCards() {
        return communityCards;
    }

    public ArrayList<Player> getWinningPlayers() {
        return winningPlayers;
    }

    public void addPlayer(String name) {
        String tempName = name;
        ArrayList<Card> tempHoleCards = new ArrayList<>(Arrays.asList(this.deck.getNextCard(), this.deck.getNextCard()));
        players.add(new Player(tempName, tempHoleCards));
    }

    // Deal community cards
    public void dealNextCommunityCard() {
        while (communityCards.size() < 5) {
            this.communityCards.add(this.deck.getNextCard());
        }
    }

    public void handleWinners() {
        for (int i = 0; i < players.size(); i++) { // Evaluate each players hand and assign a HandResult
            var currentPlayer = players.get(i);
            var currentCardsAll = playerCardAll(currentPlayer.getHoleCards(), communityCards);
            var currentHandResult = evaluateHand(currentCardsAll);
            currentPlayer.setPlayerResults(currentHandResult);
        }
        comparePlayers();
    }

    private void comparePlayers() {
        winningPlayers.clear(); // clear winning players at start of comparison

        HandResult bestResult = null;

        for (Player p : players) {
            HandResult currResult = p.getPlayerResults();

            if (bestResult == null) { // initialize bestResult with first Player object in players list
                bestResult = currResult;
                winningPlayers.add(p);
            } else {
                int compare = compareHandResults(currResult, bestResult);

                if (compare > 0) { // currResult is stronger
                    bestResult = currResult;
                    winningPlayers.clear();
                    winningPlayers.add(p);
                }
                else if (compare == 0) { // exact tie
                    winningPlayers.add(p);
                }
                // else compare<0 ignored b/c bestResult is strongest
            }
        }
    }

    private int compareHandResults (HandResult a, HandResult b) {
        int strengthA = a.getHandStrength();
        int strengthB = b.getHandStrength();

        if (strengthA != strengthB) {
            return Integer.compare(strengthA, strengthB);
        }
        var handA = a.getBestFiveCards();
        var handB = b.getBestFiveCards();

        for (int i = 0; i < 5; i++) {
            var rankA = handA.get(i).convertRankToNum();
            var rankB = handB.get(i).convertRankToNum();
            if (rankA != rankB) {
                return Integer.compare(rankA, rankB);
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Players in simulation: " + players +
                "\nTable cards: " + communityCards +
                "\nWinner: " + winningPlayers;
    }
}