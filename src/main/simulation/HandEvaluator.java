package main.simulation;

import java.lang.reflect.Array;
import java.util.*;
import main.model.*;

//TODO Calculate each players bestHand
//TODO Compare each players bestHand preflop, flop, turn, and river
//TODO Determine winner
//TODO Store results in SimulationResults

public class HandEvaluator {
    /**
     * Combines players holeCards and community cards into ArrayList resultHand
     *
     * @param holeCards
     * @param communityCards
     * @return resultHand
     */
    public ArrayList<Card> playerCardAll(ArrayList<Card> holeCards, Card[] communityCards) {
        ArrayList<Card> resultHand = new ArrayList<>();
        List<Card> tempCardList = Arrays.asList(communityCards);

        for (int i = 0; i < holeCards.size(); i++) {
            if (holeCards.get(i) != null) {
                resultHand.add(i, holeCards.get(i));
            }
        }
        for (int j = 0; j < tempCardList.size(); j++) {
            if (tempCardList.get(j) != null) {
                resultHand.add(j + holeCards.size(), tempCardList.get(j));
            }
        }
        return resultHand;
    }

    /**
     * Selection sort algorithm by suit
     *
     * @param inputHand
     * @return ArrayList<Card>
     */
    public ArrayList<Card> sortBySuit(ArrayList<Card> inputHand) {
        int minValue, i, j;
        minValue = 0;

        /**
         * Selection sort algorithm
         */
        for (i = 0; i < inputHand.size(); i++) {
            minValue = i;
            for (j = i + 1; j < inputHand.size(); j++) {
                if (inputHand.get(j).convertSuitToNum() < inputHand.get(minValue).convertSuitToNum()) {
                    minValue = j;
                }
            }
            // Swap values
            Card temp = inputHand.get(i);
            inputHand.set(i, inputHand.get(minValue));
            inputHand.set(minValue, temp);
        }
        return inputHand;
    }

    /**
     * Sort ArrayList by most present suit
     *
     * @param inputHand
     * @return inputHand
     */
    public ArrayList<Card> sortByMostPresentSuit(ArrayList<Card> inputHand) {
        HashMap<Card.Suit, Integer> suitFrequency = new HashMap<Card.Suit, Integer>();

        for (int i = 0; i < inputHand.size(); i++) {
            Card tempCard = inputHand.get(i);
            Card.Suit suit = tempCard.getSuit();

            if (!suitFrequency.containsKey(suit)) { // If suit is not contained by suitFrequency map set suit value to one
                suitFrequency.put(suit, 1);
            }
            else { // If suit is already contained by suitFrequency map increment +1
                int count = suitFrequency.get(suit);
                suitFrequency.put(suit, count + 1);
            }
        }
        Collections.sort(inputHand, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                int count1 = suitFrequency.get(o1.getSuit());
                int count2 = suitFrequency.get(o2.getSuit());

                return Integer.compare(count2, count1);
            }
        });
        return inputHand;
    }

    /**
     * Selection sort algorithm by rank
     *
     * @param inputHand
     * @return inputHand
     */
    public ArrayList<Card> sortByRank(ArrayList<Card> inputHand) {
        int minValue, i, j;
        minValue = 0;

        /**
         * Selection sort algorithm
         */
        for (i = 0; i < inputHand.size(); i++) {
            minValue = i;
            for (j = i + 1; j < inputHand.size(); j++) {
                if (inputHand.get(j).convertRankToNum() < inputHand.get(minValue).convertRankToNum()) {
                    minValue = j;
                }
            }
            // Swap values
            Card temp = inputHand.get(i);
            inputHand.set(i, inputHand.get(minValue));
            inputHand.set(minValue, temp);
        }
        return inputHand;
    }

    /**
     * Sort by most frequent rank to least frequent. In case of tie, sort by higher value rank.
     *
     * @param inputHand
     * @return inputHand
     */
    public ArrayList<Card> sortByMostPresentRank(ArrayList<Card> inputHand) {
        HashMap<Card.Rank, Integer> rankFrequency = new HashMap<Card.Rank, Integer>();

        for (int i = 0; i < inputHand.size(); i++) {
            Card tempCard = inputHand.get(i);
            Card.Rank rank = tempCard.getRank();

            if (!rankFrequency.containsKey(rank)) {
                rankFrequency.put(rank, 1);
            }
            else {
                int count = rankFrequency.get(rank);
                rankFrequency.put(rank, count + 1);
            }
        }

        Collections.sort(inputHand, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                int count1 = rankFrequency.get(o1.getRank());
                int count2 = rankFrequency.get(o2.getRank());

                if (count1 != count2) {
                    return Integer.compare(count2, count1);
                }
                else {  // Tie-breaker scenario puts higher value pair before lower pair if frequency is same
                    return Integer.compare(o2.convertRankToNum(), o1.convertRankToNum());
                }
            }
        });
        return inputHand;
    }

    /**
     * Checks if playerCardsAll contains a royal flush (A-K-Q-J-10 Suited)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isRoyalFlush(ArrayList<Card> playerCardsAll) {
        if (isStraightFlush(playerCardsAll)) {
            Set<Integer> uniqueRanks = new HashSet<>();

            for (Card card : playerCardsAll) {  // Use a set to avoid duplicate cards
                uniqueRanks.add(card.convertRankToNum());
            }

            List<Integer> sortedRanks = new ArrayList<>(uniqueRanks);   // Convert set into a sorted list
            Collections.sort(sortedRanks);

            // Check for A-K-Q-J-10 case
            if (uniqueRanks.contains(14) && uniqueRanks.contains(13) &&
                    uniqueRanks.contains(12) && uniqueRanks.contains(11) &&
                    uniqueRanks.contains(10)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if playerCardsAll contains a straight flush (2-3-4-5-6 Suited)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isStraightFlush(ArrayList<Card> playerCardsAll) {
        if (isFlush(playerCardsAll) && isStraight(playerCardsAll)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if playerCardsAll contains a four of a kind (A-A-A-A-7)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isFourOfKind(ArrayList<Card> playerCardsAll) {
        sortByMostPresentRank(playerCardsAll);

        if (playerCardsAll.get(0).getRank() == playerCardsAll.get(1).getRank() &&
                playerCardsAll.get(1).getRank() == playerCardsAll.get(2).getRank() &&
                playerCardsAll.get(2).getRank() == playerCardsAll.get(3).getRank()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if playerCardsAll contains a full house (A-A-A-K-K)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isFullHouse(ArrayList<Card> playerCardsAll) {
        sortByMostPresentRank(playerCardsAll);

        if (playerCardsAll.get(0).getRank() == playerCardsAll.get(1).getRank() &&
                playerCardsAll.get(1).getRank() == playerCardsAll.get(2).getRank()) {   //If rank repeats 3 times
            if (playerCardsAll.get(3).getRank() == playerCardsAll.get(4).getRank()) {   //If following different rank repeats twice
                return true;
            }
        }
        else if (playerCardsAll.get(0).getRank() == playerCardsAll.get(1).getRank()) {  //Else if rank repeats 2 times
            if (playerCardsAll.get(2).getRank() == playerCardsAll.get(3).getRank() &&
                    playerCardsAll.get(3).getRank() == playerCardsAll.get(4).getRank()) {   //If following differebt rank repeats 3 times
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if playerCardsAll contains a flush (5 cards of same suit)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isFlush(ArrayList<Card> playerCardsAll) {
        playerCardsAll = sortByMostPresentSuit(playerCardsAll);

        if (playerCardsAll.get(0).getSuit() == playerCardsAll.get(4).getSuit()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Checks if playerCardsAll contains a straight (2-3-4-5-6)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isStraight(ArrayList<Card> playerCardsAll) {
        Set<Integer> uniqueRanks = new HashSet<>();

        for (Card card : playerCardsAll) {  // Use a set to avoid duplicate cards
            uniqueRanks.add(card.convertRankToNum());
        }

        List<Integer> sortedRanks = new ArrayList<>(uniqueRanks);   // Convert set into a sorted list
        Collections.sort(sortedRanks);

        int consecutiveCount = 1;
        for (int i = 1; i < sortedRanks.size(); i++) {
            if (sortedRanks.get(i) == sortedRanks.get(i - 1) + 1) {
                consecutiveCount++;
                if (consecutiveCount == 5) {    // Straight found
                    return true;
                }

            }
            else if (sortedRanks.get(i) != sortedRanks.get(i - 1) + 1) {    // Reset count if not consecutive
                consecutiveCount = 1;
            }
        }
        // Check for Ace special case
        if (uniqueRanks.contains(14) && uniqueRanks.contains(2) &&
                uniqueRanks.contains(3) && uniqueRanks.contains(4) &&
                uniqueRanks.contains(5)) {
            return true;
        }

        return false;
    }

    /**
     * Checks if playerCardsAll contains a three of a kind (4-4-4-K-J)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isThreeOfKind(ArrayList<Card> playerCardsAll) {
        sortByMostPresentRank(playerCardsAll);

        if (playerCardsAll.get(0).getRank() == playerCardsAll.get(1).getRank() &&
                playerCardsAll.get(1).getRank() == playerCardsAll.get(2).getRank()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if playerCardsAll contains a two pair (2-2-3-3-A)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isTwoPair(ArrayList<Card> playerCardsAll) {
        sortByMostPresentRank(playerCardsAll);

        if (playerCardsAll.get(0).getRank() == playerCardsAll.get(1).getRank() &&
                playerCardsAll.get(2).getRank() == playerCardsAll.get(3).getRank()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if playerCardsAll contains a one pair (2-2-5-7-K)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isOnePair(ArrayList<Card> playerCardsAll) {
        sortByMostPresentRank(playerCardsAll);

        if (playerCardsAll.get(0).getRank() == playerCardsAll.get(1).getRank()) {
            return true;
        }

        return false;
    }

    public ArrayList<Card> compareHighCard(ArrayList<Card> list1, ArrayList<Card> list2) {
        if (list1.size() != list2.size()) {
            return null;
        }

        HashMap<Card.Rank, Integer> myMap = new HashMap<>();
        for (int i = 0; i < list1.size(); i++) {
            Card.Rank rank = list1.get(i).getRank();
            myMap.put(rank, myMap.getOrDefault(rank, 0) + 1);
        }

        return null;
    }
}