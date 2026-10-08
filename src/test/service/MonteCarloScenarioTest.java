package test.service;

import main.model.Card;
import main.service.MonteCarloScenario;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonteCarloScenarioTest {

    @Test
    void constructor_storesScenarioValues() {

        List<Card> heroCards = List.of(
                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                new Card(Card.Rank.KING, Card.Suit.SPADE)
        );

        List<Card> board = List.of(
                new Card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.JACK, Card.Suit.CLUB),
                new Card(Card.Rank.FOUR, Card.Suit.SPADE)
        );

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        heroCards,
                        board,
                        1000,
                        2
                );

        assertEquals(heroCards, scenario.getHeroCards());
        assertEquals(board, scenario.getKnownCommunityCards());
        assertEquals(1000, scenario.getNumTrials());
        assertEquals(2, scenario.getNumOpponents());
    }

    @Test
    void constructor_defensivelyCopiesHeroCards() {

        List<Card> heroCards = new ArrayList<>();

        heroCards.add(
                new Card(Card.Rank.ACE, Card.Suit.SPADE)
        );

        heroCards.add(
                new Card(Card.Rank.KING, Card.Suit.SPADE)
        );

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        heroCards,
                        List.of(),
                        100,
                        1
                );

        heroCards.clear();

        assertEquals(2, scenario.getHeroCards().size());
    }

    @Test
    void constructor_defensivelyCopiesCommunityCards() {

        List<Card> board = new ArrayList<>();

        board.add(
                new Card(Card.Rank.QUEEN, Card.Suit.DIAMOND)
        );

        board.add(
                new Card(Card.Rank.JACK, Card.Suit.CLUB)
        );

        board.add(
                new Card(Card.Rank.FOUR, Card.Suit.SPADE)
        );

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        List.of(
                                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                                new Card(Card.Rank.KING, Card.Suit.SPADE)
                        ),
                        board,
                        100,
                        1
                );

        board.clear();

        assertEquals(
                3,
                scenario.getKnownCommunityCards().size()
        );
    }

    @Test
    void heroCards_cannotBeModifiedThroughGetter() {

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        List.of(
                                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                                new Card(Card.Rank.KING, Card.Suit.SPADE)
                        ),
                        List.of(),
                        100,
                        1
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> scenario.getHeroCards().add(
                        new Card(
                                Card.Rank.QUEEN,
                                Card.Suit.CLUB
                        )
                )
        );
    }
}