package main.simulation;
import java.util.ArrayList;
import java.util.List;
import main.model.*;

public class SimulationResults {
    private ArrayList<Player> playersList;
    private ArrayList<Player> winningPlayers;
    private ArrayList<Card> winningHoleCards;
    private ArrayList<Card> winningHand;
    private List<Card> communityCards;

    public SimulationResults(ArrayList<Player> playersList, ArrayList<Player> winningPlayers, List<Card> communityCards) {
        this.playersList = playersList;
        this.winningPlayers = winningPlayers;
        this.communityCards = communityCards;
        this.winningHoleCards = null;
        this.winningHand = null;
        // TODO assign winningHoleCards and winningHand with a value --> implement an extractWinningHand() method
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }

    public ArrayList<Player> getWinningPlayers() {
        return winningPlayers;
    }

    public void setWinningPlayer(ArrayList<Player> winningPlayers) {
        this.winningPlayers = winningPlayers;
    }

    public ArrayList<Card> getWinningHoleCards() {
        return winningHoleCards;
    }

    public void setWinningHoleCards(ArrayList<Card> winningHoleCards) {
        this.winningHoleCards = winningHoleCards;
    }

    public ArrayList<Card> getWinningHand() {
        return winningHand;
    }

    public void setWinningHand(ArrayList<Card> winningHand) {
        this.winningHand = winningHand;
    }

}