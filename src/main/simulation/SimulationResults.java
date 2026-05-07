package main.simulation;
import java.util.ArrayList;
import java.util.List;
import main.model.*;

public class SimulationResults {
    private ArrayList<Player> playersList;
    private ArrayList<Player> winningPlayers;
    private List<List<Card>> winningHoleCards;
    private List<List<Card>> winningHands;

    public ArrayList<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(ArrayList<Card> communityCards) {
        this.communityCards = communityCards;
    }

    private ArrayList<Card> communityCards;

    public SimulationResults(ArrayList<Player> playersList, ArrayList<Player> winningPlayers, ArrayList<Card> communityCards) {
        this.playersList = playersList;
        this.winningPlayers = winningPlayers;
        this.communityCards = communityCards;

        this.winningHoleCards = new ArrayList<>();
        this.winningHands = new ArrayList<>();

        for (Player p : winningPlayers) {
            p.setWinner(true);
            winningHoleCards.add(new ArrayList<>(p.getHoleCards()));
            winningHands.add(new ArrayList<>(p.getPlayerResults().getBestFiveCards()));
        }
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

    public List<List<Card>> getWinningHoleCards() {
        return winningHoleCards;
    }

    public void setWinningHoleCards(List<List<Card>> winningHoleCards) {
        this.winningHoleCards = winningHoleCards;
    }

    public List<List<Card>> getWinningHand() {
        return winningHands;
    }

    public void setWinningHand(List<List<Card>> winningHand) {
        this.winningHands = winningHands;
    }

}