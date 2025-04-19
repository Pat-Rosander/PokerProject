package main.model;

import main.simulation.PokerSimulation;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        PokerSimulation simulation = new PokerSimulation();
        /*
        simulation.playerSetup();
        simulation.dealNextCommunityCard();
        simulation.dealNextCommunityCard();
        simulation.dealNextCommunityCard();
        Card[] communityCards = simulation.getCommunityCards();
        System.out.println("table cards: ");

        for (int i = 0; i < communityCards.length; i++) {
            System.out.println(communityCards[i]);
        }

        Player player1 = simulation.getPlayers().get(0);
        System.out.println("player1 hole cards");
        System.out.println(player1.getHoleCards());

        System.out.println("Sorting player1 hole cards by suit:");
        System.out.println(simulation.sortBySuit(player1.getHoleCards()));
        System.out.println("Sorting player1 hole cards by rank:");
        System.out.println(simulation.sortByRank(player1.getHoleCards()));

        ArrayList <Card> newHand = simulation.playerCardAll(player1.getHoleCards(), communityCards);

        System.out.println("Player1 hole cards and community cards");
        System.out.println(newHand);

        newHand = simulation.sortByMostPresentRank(newHand);

        System.out.println("player1 hole cards and community cards sorted by most present rank");
        System.out.println(newHand);

         */



    }
}