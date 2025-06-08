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

        comparePlayers();
    }

    private void comparePlayers() {
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
                ArrayList<Card> winningHand = compareHighCard(highestValuePlayer.getPlayerResults().getBestFiveCards(), currPlayer.getPlayerResults().getBestFiveCards());
                if (winningHand == null) {
                    winningPlayers.add(currPlayer);
                    break;
                }
                else if (highestValuePlayer.getPlayerResults().getBestFiveCards() != winningHand) {
                    highestValuePlayer = currPlayer;
                    highestValue = currValue;
                }
            }
        }
        winningPlayers.add(highestValuePlayer);
    }

    @Override
    public String toString() {
        return "Players in simulation: " + players +
                " Table cards: " + communityCards +
                " Winner: " + winningPlayers;
    }
}