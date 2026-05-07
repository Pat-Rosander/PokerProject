package main.simulation;

import java.util.*;
import main.model.*;

public class HandEvaluator {
    /**
     * Combines players holeCards and community cards into ArrayList resultHand
     *
     * @param holeCards
     * @param communityCards
     * @return resultHand
     */
    public ArrayList<Card> playerCardAll(ArrayList<Card> holeCards, ArrayList<Card> communityCards) {
        ArrayList<Card> resultHand = new ArrayList<>();

        for (int i = 0; i < holeCards.size(); i++) {
            if (holeCards.get(i) != null) {
                resultHand.add(i, holeCards.get(i));
            }
        }
        for (int j = 0; j < communityCards.size(); j++) {
            if (communityCards.get(j) != null) {
                resultHand.add(j + holeCards.size(), communityCards.get(j));
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
     * Selection sort algorithm by ascending rank
     *
     * @param inputHand
     * @return inputHand
     */
    public ArrayList<Card> sortByAscendingRank(ArrayList<Card> inputHand) {
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
     * Selection sort algorithm by descending rank
     *
     * @param cards
     * @return copy
     */
    public ArrayList<Card> sortByDescendingRank(List<Card> cards) {
        ArrayList<Card> copy = new ArrayList<>(cards);
        int maxValue, i, j;
        maxValue = 0;

        /**
         * Selection sort algorithm
         */
        for (i = 0; i < copy.size(); i++) {
            maxValue = i;
            for (j = i + 1; j < copy.size(); j++) {
                if (copy.get(j).convertRankToNum() > copy.get(maxValue).convertRankToNum()) {
                    maxValue = j;
                }
            }
            // Swap values
            Card temp = copy.get(i);
            copy.set(i, copy.get(maxValue));
            copy.set(maxValue, temp);
        }
        return copy;
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
        ArrayList<Card> copy = new ArrayList<>(playerCardsAll);
        sortByMostPresentRank(copy);

        if (copy.get(0).getRank() == copy.get(1).getRank() &&
                copy.get(1).getRank() == copy.get(2).getRank() &&
                copy.get(2).getRank() == copy.get(3).getRank()) {
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
        ArrayList<Card> copy = new ArrayList<>(playerCardsAll);
        sortByMostPresentRank(copy);

        if (copy.get(0).getRank() == copy.get(1).getRank() &&
                copy.get(1).getRank() == copy.get(2).getRank()) {   //If rank repeats 3 times
            if (copy.get(3).getRank() == copy.get(4).getRank()) {   //If following different rank repeats twice
                return true;
            }
        }
        else if (copy.get(0).getRank() == copy.get(1).getRank()) {  //Else if rank repeats 2 times
            if (copy.get(2).getRank() == copy.get(3).getRank() &&
                    copy.get(3).getRank() == copy.get(4).getRank()) {   //If following differebt rank repeats 3 times
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
        ArrayList<Card> copy = new ArrayList<>(playerCardsAll);
        sortByMostPresentSuit(copy);

        if (copy.get(0).getSuit() == copy.get(4).getSuit()) {
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
            else {
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
     * Checks if straight is Ace low
     * @param playerCardsAll
     * @return
     */
    public Boolean isAceLowStraight(ArrayList<Card> playerCardsAll) {
            // TODO implement this method if necessary
        return false;
    }

    /**
     * Checks if playerCardsAll contains a three of a kind (4-4-4-K-J)
     *
     * @param playerCardsAll
     * @return Boolean
     */
    public Boolean isThreeOfKind(ArrayList<Card> playerCardsAll) {
        ArrayList<Card> copy = new ArrayList<>(playerCardsAll);
        sortByMostPresentRank(copy);

        if (copy.get(0).getRank() == copy.get(1).getRank() &&
                copy.get(1).getRank() == copy.get(2).getRank()) {
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
        ArrayList<Card> copy = new ArrayList<>(playerCardsAll);
        sortByMostPresentRank(copy);

        if (copy.get(0).getRank() == copy.get(1).getRank() &&
                copy.get(2).getRank() == copy.get(3).getRank()) {
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
        ArrayList<Card> copy = new ArrayList<>(playerCardsAll);
        sortByMostPresentRank(copy);

        if (copy.get(0).getRank() == copy.get(1).getRank()) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param all7
     * @return
     */
    public HandResult evaluateHand(ArrayList<Card> all7) {
        var copy = new ArrayList<Card>(all7);
        var result = new HandResult();
        var best = new ArrayList<Card>();

        best = findRoyalFlush(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.ROYAL_FLUSH);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best); // find best hand for royal flush (5 card list that is A-K-Q-J-10 suited)
            return result;
        }

        best = findStraightFlush(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.STRAIGHT_FLUSH);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for straight flush (5 card list that is flush and straight)
            return result;
        }

        best = findQuads(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.FOUR_OF_A_KIND);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for four of a kind (4 card list of same rank + highest rank kicker)
            return result;
        }

        best = findFullHouse(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.FULL_HOUSE);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for full house (5 card list of 3 some x rank and 2 some y rank)
            return result;
        }

        best = findFlush(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.FLUSH);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for flush (5 card list of same suit)
            return result;
        }

        best = findStraight(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.STRAIGHT);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for straight (5 card list of consecutive increasing rank)
            return result;
        }

        best = findTrips(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.THREE_OF_A_KIND);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for three of a kind (3 card list of same rank + 2 highest kicker)
            return result;
        }

        best = findTwoPair(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.TWO_PAIR);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best);// find best hand for two pair (4 card list of 2 some x rank and 2 some y rank + highest kicker)
            return result;
        }

        best = findPair(copy);
        if (best.size() == 5) {
            result.setRank(HandRank.PAIR);
            result.setHandStrength(result.convertHandRankToNum());
            result.setBestFiveCards(best); // find best hand for pair (2 card list of same rank + 3 highest kicker)
            return result;
        }

        best = findHighCard(copy);
        result.setRank(HandRank.HIGH_CARD);
        result.setHandStrength(result.convertHandRankToNum());
        result.setBestFiveCards(best);
        return result;
    }

    /**
     * extracts five highest cards
     * @param all7
     * @return
     */
    private ArrayList<Card> findHighCard(ArrayList<Card> all7) {
        var copy = new ArrayList<>(all7);
        var sorted = sortByDescendingRank(copy);
        return new ArrayList<>(sorted.subList(0, 5));
    }

    /**
     * only called if isOnePair(all7) is true
     * extracts pair and 3 highest cards
     * @param all7
     * @return
     */
    private ArrayList<Card> findPair(ArrayList<Card> all7) {
        var copy = new ArrayList<>(all7);
        copy = sortByMostPresentRank(copy);
        var sorted = new ArrayList<Card>(5);

        if (copy.get(0).getRank() == copy.get(1).getRank()) {
            // add pair
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
            // remove from high to low so indices don't shift incorrectly
            copy.remove(1);
            copy.remove(0);

            // extract three highest value cards remaining
            copy = sortByDescendingRank(copy);
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
            sorted.add(copy.get(2));
        }
        return sorted;
    }

    /**
     * only called if isTwoPair(all7) is true
     * extracts two pair and highest card
     * @param all7
     * @return
     */
    private ArrayList<Card> findTwoPair(ArrayList<Card> all7) {
        var copy = new ArrayList<Card>(all7);
        copy = sortByMostPresentRank(copy);
        var sorted = new ArrayList<Card>(5);

        if (copy.get(0).getRank() == copy.get(1).getRank() && copy.get(2).getRank() == copy.get(3).getRank()) {
            // add both pairs
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
            sorted.add(copy.get(2));
            sorted.add(copy.get(3));
            // remove from high to low so indices don't shift incorrectly
            copy.remove(3);
            copy.remove(2);
            copy.remove(1);
            copy.remove(0);

            // extract highest value card remaining
            copy = sortByDescendingRank(copy);
            sorted.add(copy.get(0));
        }
        return sorted;
    }

    /**
     * only called if isThreeOfAKind(all7) is true
     * extracts trips and two highest cards
     * @param all7
     * @return
     */
    private ArrayList<Card> findTrips(ArrayList<Card> all7) {
        var copy = new ArrayList<Card>(all7);
        copy = sortByMostPresentRank(copy);
        var sorted = new ArrayList<Card>(5);

        if (copy.get(0).getRank() == copy.get(1).getRank() && copy.get(1).getRank() == copy.get(2).getRank()) {
            // add trips
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
            sorted.add(copy.get(2));

            // remove from high to low so indices don't shift incorrectly
            copy.remove(2);
            copy.remove(1);
            copy.remove(0);

            // extract two highest value cards remaining
            copy = sortByDescendingRank(copy);
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
        }
        return sorted;
    }

    /**
     * only called if isStraight(all7) is true
     * extracts best straight (including A-5 wheel)
     * @param all7
     * @return
     */
    private ArrayList<Card> findStraight(ArrayList<Card> all7) {
        Map<Integer, Card> cardByRank = new HashMap<>();
        Set<Integer> rankSet = new HashSet<>();

        for (Card c : all7) { // no duplicate ranks
            int r = c.convertRankToNum();
            rankSet.add(r);
            cardByRank.putIfAbsent(r, c);
        }

        List<Integer> ranks = new ArrayList<>(rankSet);
        Collections.sort(ranks);

        List<Integer> bestRun = new ArrayList<>();
        List<Integer> currentRun = new ArrayList<>();

        for (int r : ranks) {
            if (currentRun.isEmpty()) { // start run
                currentRun.add(r);
            }
            else {
                int last = currentRun.get(currentRun.size() - 1); // look at previous rank
                if (r == last + 1) { // if r continues straight
                    currentRun.add(r);
                } else if (r != last) { // if r breaks straight
                    currentRun.clear();
                    currentRun.add(r);
                }
            }
            if (currentRun.size() >= 5) {
                bestRun = new ArrayList<>(currentRun);
            }
        }

        // A-2-3-4-5 wheel straight
        boolean hasWheel = rankSet.contains(14) &&
                        rankSet.contains(2) &&
                        rankSet.contains(3) &&
                        rankSet.contains(4) &&
                        rankSet.contains(5);

        ArrayList<Card> result = new ArrayList<Card>(5);

        if (!bestRun.isEmpty()) { // perfer highest normal straight that beats wheel
            List<Integer> needed = bestRun.subList(bestRun.size() - 5, bestRun.size());
            for (int i = needed.size() - 1; i >= 0; i--) {
                int r = needed.get(i);
                result.add(cardByRank.get(r));
            }
            return result;
        }

        if (hasWheel) { // if no normal straight, check if wheel is present
            int[] wheelRanks = {5, 4, 3, 2, 14};
            for (int r: wheelRanks) {
                result.add(cardByRank.get(r));
            }
            return result;
        }
        // no straight
        return result;
    }

    /**
     * only called if isFlush(all7) is true
     * extracts highest value flush
     * @param all7
     * @return
     */
    private ArrayList<Card> findFlush (ArrayList<Card> all7) {
        var sorted = new ArrayList<Card>(5);
        Map<Card.Suit, ArrayList<Card>> suitGroups = new HashMap<>();
        ArrayList<Card> flushSuitCards = null;

        for (Card c : all7) {
            suitGroups.computeIfAbsent(c.getSuit(), s -> new ArrayList<>()).add(c);
        }

        for (ArrayList<Card> group : suitGroups.values()) {
            if (group.size() >= 5) {
                flushSuitCards = group;
                break;
            }
        }

        if (flushSuitCards == null) {
            return new ArrayList<>();
        }

        sorted = sortByDescendingRank(flushSuitCards);
        return new ArrayList<>(sorted.subList(0, 5));
    }

    /**
     * only called if isFullHouse(all7) is true
     * extracts full house (trips and a pair)
     * @param all7
     * @return
     */
    private ArrayList<Card> findFullHouse (ArrayList<Card> all7) {
        var copy = new ArrayList<Card>(all7);
        copy = sortByMostPresentRank(copy);
        var sorted = new ArrayList<Card>(5);

        if (copy.get(0).getRank() == copy.get(1).getRank() &&
                copy.get(1).getRank() == copy.get(2).getRank() &&
                copy.get(3).getRank() == copy.get(4).getRank()) {
            // add full house
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
            sorted.add(copy.get(2));
            sorted.add(copy.get(3));
            sorted.add(copy.get(4));
        }
        return sorted;
    }

    /**
     * only called if isFourOfAKind(all7) is true
     * extracts quads and highest card
     * @param all7
     * @return
     */
    private ArrayList<Card> findQuads(ArrayList<Card> all7) {
        var copy = new ArrayList<Card>(all7);
        copy = sortByMostPresentRank(copy);
        var sorted = new ArrayList<Card>(5);

        if (copy.get(0).getRank() == copy.get(1).getRank() &&
                copy.get(1).getRank() == copy.get(2).getRank() &&
                copy.get(2).getRank() == copy.get(3).getRank()) {
            // add quads
            sorted.add(copy.get(0));
            sorted.add(copy.get(1));
            sorted.add(copy.get(2));
            sorted.add(copy.get(3));

            // remove from high to low
            copy.remove(3);
            copy.remove(2);
            copy.remove(1);
            copy.remove(0);

            // extract highest remaining value card
            copy = sortByDescendingRank(copy);
            sorted.add(copy.get(0));
        }
        return sorted;
    }

    /**
     *
     * @param all7
     * @return
     */
    private ArrayList<Card> findStraightFlush (ArrayList<Card> all7) {
        Map<Card.Suit, ArrayList<Card>> suitGroups = new HashMap<>();
        for (Card c : all7) {
            // if map contains suit, return list, and append c to list
            // if map does not contain suit, execute lambda, insert newly created list into map,
            // return new list, and append c to list
            suitGroups.computeIfAbsent(c.getSuit(), s -> new ArrayList<>()).add(c);
        }

        ArrayList<Card> flushSuitCards = null;
        for (ArrayList<Card> group : suitGroups.values()) {
            // if any suitGroup has length greater or equal to 5, assign it to flushSuitCards, and break
            if (group.size() >= 5) {
                flushSuitCards = group;
                break;
            }
        }
        if (flushSuitCards == null) {
            return new ArrayList<Card>();
        }

        return findStraight(flushSuitCards);
    }

    /**
     *
     * @param all7
     * @return
     */
    private ArrayList<Card> findRoyalFlush (ArrayList<Card> all7) {
        ArrayList<Card> copy = findStraightFlush(all7);

        if (copy.size() != 5) {
            return new ArrayList<Card>();
        }

        int high = copy.get(0).convertRankToNum();
        int low = copy.get(4).convertRankToNum();

        if (low == 10 && high == 14) {
            return copy;
        }

        return new ArrayList<Card>();
    }
}