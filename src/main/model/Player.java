package main.model;

import main.model.Card;
import main.simulation.SimulationResults;

import java.util.ArrayList;
import java.util.List;
public class Player {
    private String name;
    private ArrayList<Card> holeCards; // main.model.Player has a list of two hole cards
    private SimulationResults playerResults;

    public Player(String name, ArrayList<Card> holeCards) {
        this.name = name;
        this.holeCards = holeCards;
    }

    //Setter methods
    public void setName (String name) {
        this.name = name;
    }
    public void setHoleCards(ArrayList<Card> holeCards) {
        this.holeCards = holeCards;
    }
    public void setPlayerResults(SimulationResults playerResults) {
        this.playerResults = playerResults;
    }
    //Getter methods
    public String getName() {
        return name;
    }
    public ArrayList<Card> getHoleCards() {
        return holeCards;
    }
    public SimulationResults getPlayerResults() {
        return playerResults;
    }
    //toString displays player info
    @Override
    public String toString() {
        return "Player: " + this.name + " Hole Cards: " + this.holeCards;
    }
}
