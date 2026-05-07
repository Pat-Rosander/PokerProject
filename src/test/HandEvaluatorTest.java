package test;

import main.model.Card;
import main.simulation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HandEvaluatorTest {
    @Test
    @DisplayName("Royal flush bestFiveCards are ordered A K Q J 10")
    void royalFlushBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.JACK, Card.Suit.HEART),
                card(Card.Rank.TEN, Card.Suit.HEART),
                card(Card.Rank.THREE, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.DIAMOND)
        );

        assertHand(HandRank.ROYAL_FLUSH, result,
                Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.JACK, Card.Rank.TEN);
    }

    @Test
    @DisplayName("Straight flush bestFiveCards are ordered by straight high card")
    void straightFlushBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.NINE, Card.Suit.SPADE),
                card(Card.Rank.EIGHT, Card.Suit.SPADE),
                card(Card.Rank.SEVEN, Card.Suit.SPADE),
                card(Card.Rank.SIX, Card.Suit.SPADE),
                card(Card.Rank.FIVE, Card.Suit.SPADE),
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.KING, Card.Suit.DIAMOND)
        );

        assertHand(HandRank.STRAIGHT_FLUSH, result,
                Card.Rank.NINE, Card.Rank.EIGHT, Card.Rank.SEVEN, Card.Rank.SIX, Card.Rank.FIVE);
    }

    @Test
    @DisplayName("Four of a kind bestFiveCards are ordered quads first, then kicker")
    void fourOfAKindBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.NINE, Card.Suit.SPADE),
                card(Card.Rank.NINE, Card.Suit.HEART),
                card(Card.Rank.NINE, Card.Suit.CLUB),
                card(Card.Rank.NINE, Card.Suit.DIAMOND),
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.KING, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.DIAMOND)
        );

        assertHand(HandRank.FOUR_OF_A_KIND, result,
                Card.Rank.NINE, Card.Rank.NINE, Card.Rank.NINE, Card.Rank.NINE, Card.Rank.ACE);
    }

    @Test
    @DisplayName("Full house bestFiveCards are ordered trips first, then pair")
    void fullHouseBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.KING, Card.Suit.SPADE),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.CLUB),
                card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.TWO, Card.Suit.CLUB)
        );

        assertHand(HandRank.FULL_HOUSE, result,
                Card.Rank.KING, Card.Rank.KING, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.QUEEN);
    }

    @Test
    @DisplayName("Flush bestFiveCards are ordered descending values")
    void flushBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.NINE, Card.Suit.HEART),
                card(Card.Rank.SIX, Card.Suit.HEART),
                card(Card.Rank.THREE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.DIAMOND),
                card(Card.Rank.TWO, Card.Suit.CLUB)
        );

        assertHand(HandRank.FLUSH, result,
                Card.Rank.ACE, Card.Rank.QUEEN, Card.Rank.NINE, Card.Rank.SIX, Card.Rank.THREE);
    }

    @Test
    @DisplayName("Straight bestFiveCards are ordered by straight high card")
    void straightBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.NINE, Card.Suit.SPADE),
                card(Card.Rank.EIGHT, Card.Suit.HEART),
                card(Card.Rank.SEVEN, Card.Suit.CLUB),
                card(Card.Rank.SIX, Card.Suit.DIAMOND),
                card(Card.Rank.FIVE, Card.Suit.SPADE),
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.KING, Card.Suit.DIAMOND)
        );

        assertHand(HandRank.STRAIGHT, result,
                Card.Rank.NINE, Card.Rank.EIGHT, Card.Rank.SEVEN, Card.Rank.SIX, Card.Rank.FIVE);
    }

    @Test
    @DisplayName("Ace-low straight bestFiveCards are ordered 5 4 3 2 A")
    void aceLowStraightBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.FIVE, Card.Suit.SPADE),
                card(Card.Rank.FOUR, Card.Suit.HEART),
                card(Card.Rank.THREE, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.DIAMOND),
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.DIAMOND),
                card(Card.Rank.NINE, Card.Suit.CLUB)
        );

        assertHand(HandRank.STRAIGHT, result,
                Card.Rank.FIVE, Card.Rank.FOUR, Card.Rank.THREE, Card.Rank.TWO, Card.Rank.ACE);
    }

    @Test
    @DisplayName("Three of a kind bestFiveCards are ordered trips first, then kickers descending")
    void threeOfAKindBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.EIGHT, Card.Suit.SPADE),
                card(Card.Rank.EIGHT, Card.Suit.HEART),
                card(Card.Rank.EIGHT, Card.Suit.CLUB),
                card(Card.Rank.ACE, Card.Suit.DIAMOND),
                card(Card.Rank.KING, Card.Suit.CLUB),
                card(Card.Rank.THREE, Card.Suit.SPADE),
                card(Card.Rank.TWO, Card.Suit.HEART)
        );

        assertHand(HandRank.THREE_OF_A_KIND, result,
                Card.Rank.EIGHT, Card.Rank.EIGHT, Card.Rank.EIGHT, Card.Rank.ACE, Card.Rank.KING);
    }

    @Test
    @DisplayName("Two pair bestFiveCards are ordered high pair, low pair, then kicker")
    void twoPairBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.JACK, Card.Suit.SPADE),
                card(Card.Rank.JACK, Card.Suit.HEART),
                card(Card.Rank.SEVEN, Card.Suit.CLUB),
                card(Card.Rank.SEVEN, Card.Suit.DIAMOND),
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.KING, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.DIAMOND)
        );

        assertHand(HandRank.TWO_PAIR, result,
                Card.Rank.JACK, Card.Rank.JACK, Card.Rank.SEVEN, Card.Rank.SEVEN, Card.Rank.ACE);
    }

    @Test
    @DisplayName("Pair bestFiveCards are ordered pair first, then kickers descending")
    void pairBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.SPADE),
                card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                card(Card.Rank.TEN, Card.Suit.DIAMOND),
                card(Card.Rank.SIX, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.SPADE)
        );

        assertHand(HandRank.PAIR, result,
                Card.Rank.ACE, Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.TEN);
    }

    @Test
    @DisplayName("High card bestFiveCards are ordered descending values")
    void highCardBestFiveCardsOrder() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.QUEEN, Card.Suit.SPADE),
                card(Card.Rank.TEN, Card.Suit.DIAMOND),
                card(Card.Rank.EIGHT, Card.Suit.DIAMOND),
                card(Card.Rank.SIX, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.SPADE)
        );

        assertHand(HandRank.HIGH_CARD, result,
                Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.TEN, Card.Rank.EIGHT);
    }

    @Test
    @DisplayName("Full house with two possible trips chooses higher trips then pair")
    void fullHouseWithTwoPossibleTripsChoosesBestHouse() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.SPADE),
                card(Card.Rank.KING, Card.Suit.CLUB),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.TWO, Card.Suit.DIAMOND)
        );

        assertHand(HandRank.FULL_HOUSE, result,
                Card.Rank.ACE, Card.Rank.ACE, Card.Rank.ACE, Card.Rank.KING, Card.Rank.KING);
    }

    @Test
    @DisplayName("Full house with one trips and multiple pairs chooses highest pair")
    void fullHouseWithMultiplePairsChoosesBestHouse() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.SPADE),
                card(Card.Rank.KING, Card.Suit.CLUB),
                card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                card(Card.Rank.QUEEN, Card.Suit.HEART)
        );

        assertHand(HandRank.FULL_HOUSE, result,
                Card.Rank.ACE, Card.Rank.ACE, Card.Rank.ACE, Card.Rank.KING, Card.Rank.KING);
    }

    @Test
    @DisplayName("Flush with more than five suited cards chooses highest five")
    void flushWithMoreThanFiveSuitedCardsChoosesBestFive() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.EIGHT, Card.Suit.HEART),
                card(Card.Rank.FIVE, Card.Suit.HEART),
                card(Card.Rank.THREE, Card.Suit.HEART),
                card(Card.Rank.TWO, Card.Suit.CLUB)
        );

        assertHand(HandRank.FLUSH, result,
                Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.EIGHT, Card.Rank.FIVE);
    }

    @Test
    @DisplayName("Straight with duplicate ranks ignores duplicates")
    void straightWithDuplicateRanksUsesUniqueRanks() {
        HandResult result = evaluate(
                card(Card.Rank.NINE, Card.Suit.SPADE),
                card(Card.Rank.EIGHT, Card.Suit.CLUB),
                card(Card.Rank.EIGHT, Card.Suit.HEART),
                card(Card.Rank.SEVEN, Card.Suit.DIAMOND),
                card(Card.Rank.SIX, Card.Suit.SPADE),
                card(Card.Rank.FIVE, Card.Suit.HEART),
                card(Card.Rank.TWO, Card.Suit.CLUB)
        );

        assertHand(HandRank.STRAIGHT, result,
                Card.Rank.NINE, Card.Rank.EIGHT, Card.Rank.SEVEN, Card.Rank.SIX, Card.Rank.FIVE);
    }

    @Test
    @DisplayName("Straight chooses highest possible straight")
    void straightChoosesHighestPossibleStraight() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.KING, Card.Suit.DIAMOND),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.JACK, Card.Suit.SPADE),
                card(Card.Rank.TEN, Card.Suit.CLUB),
                card(Card.Rank.NINE, Card.Suit.DIAMOND),
                card(Card.Rank.EIGHT, Card.Suit.HEART)
        );

        assertHand(HandRank.STRAIGHT, result,
                Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.JACK, Card.Rank.TEN);
    }

    @Test
    @DisplayName("Straight flush with seven suited broadway cards returns royal flush")
    void straightFlushChoosesHighestStraightFlush() {
        HandResult result = evaluate(
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.JACK, Card.Suit.HEART),
                card(Card.Rank.TEN, Card.Suit.HEART),
                card(Card.Rank.NINE, Card.Suit.HEART),
                card(Card.Rank.EIGHT, Card.Suit.HEART)
        );

        assertHand(HandRank.ROYAL_FLUSH, result,
                Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.JACK, Card.Rank.TEN);
    }

    private Card card(Card.Rank rank, Card.Suit suit) {
        return new Card(rank, suit);
    }

    private ArrayList<Card> cards(Card... cards) {
        return new ArrayList<>(Arrays.asList(cards));
    }

    private HandResult evaluate(Card... inputCards) {
        return new HandEvaluator().evaluateHand(cards(inputCards));
    }

    private void assertHand(HandRank expectedRank, HandResult result, Card.Rank... expectedRanks) {
        assertEquals(expectedRank, result.getRank());
        assertBestRanks(result, expectedRanks);
    }

    private void assertBestRanks(HandResult result, Card.Rank... expectedRanks) {
        ArrayList<Card> best = result.getBestFiveCards();
        assertEquals(5, best.size(), "bestFiveCards should always contain five cards");

        for (int i = 0; i < expectedRanks.length; i++) {
            assertEquals(expectedRanks[i], best.get(i).getRank(), "rank mismatch at bestFiveCards index " + i);
        }
    }
}
