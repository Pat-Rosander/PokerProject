package main.simulation;
import java.util.ArrayList;
import main.model.*;

public class SimulationResults {
    private ArrayList<Player> playersList;
    private Player winningPlayer;
    private ArrayList<Card> winningHoleCards;
    private ArrayList<Card> winningHand;
    private int serial;
    private static int nextSerialNumber = 1;

    public SimulationResults(ArrayList<Player> playersList, Player winningPlayer, ArrayList<Card> winningHoleCards, ArrayList<Card> winningHand) {
        this.playersList = playersList;
        this.winningPlayer = winningPlayer;
        this.winningHoleCards = winningHoleCards;
        this.winningHand = winningHand;
        this.serial = getNextSerialNumber();
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(Player winningPlayer) {
        this.winningPlayer = winningPlayer;
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

    public static int getNextSerialNumber() {
        return ++nextSerialNumber;
    }
}