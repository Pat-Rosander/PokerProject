package test.service;

import main.model.Card;
import main.service.MonteCarloResults;
import main.service.MonteCarloScenario;
import main.service.MonteCarloSimulationService;
import main.simulation.HandRank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonteCarloSimulationServiceTest {

    private final MonteCarloSimulationService service =
            new MonteCarloSimulationService();

    @Test
    void runSimulation_rejectsNullScenario() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(null)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    void runSimulation_rejectsNonPositiveTrialCount(
            int numTrials
    ) {

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        validHeroCards(),
                        List.of(),
                        numTrials,
                        1
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(scenario)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 10})
    void runSimulation_rejectsInvalidOpponentCount(
            int numOpponents
    ) {

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        validHeroCards(),
                        List.of(),
                        10,
                        numOpponents
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(scenario)
        );
    }

    @Test
    void runSimulation_rejectsInvalidHeroCardCount() {

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        List.of(
                                new Card(
                                        Card.Rank.ACE,
                                        Card.Suit.SPADE
                                )
                        ),
                        List.of(),
                        10,
                        1
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(scenario)
        );
    }

    @Test
    void runSimulation_rejectsDuplicateHeroCards() {

        Card aceSpades =
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.SPADE
                );

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        List.of(
                                aceSpades,
                                aceSpades
                        ),
                        List.of(),
                        10,
                        1
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(scenario)
        );
    }

    @Test
    void runSimulation_rejectsHeroCardDuplicatedOnBoard() {

        Card aceSpades =
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.SPADE
                );

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        List.of(
                                aceSpades,
                                new Card(
                                        Card.Rank.KING,
                                        Card.Suit.SPADE
                                )
                        ),
                        List.of(
                                aceSpades,
                                new Card(
                                        Card.Rank.JACK,
                                        Card.Suit.CLUB
                                ),
                                new Card(
                                        Card.Rank.FOUR,
                                        Card.Suit.DIAMOND
                                )
                        ),
                        10,
                        1
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(scenario)
        );
    }

    @Test
    void runSimulation_rejectsInvalidBoardSize() {

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        validHeroCards(),
                        List.of(
                                new Card(
                                        Card.Rank.QUEEN,
                                        Card.Suit.DIAMOND
                                )
                        ),
                        10,
                        1
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.runSimulation(scenario)
        );
    }

    @Test
    void runSimulation_guaranteedRoyalFlushHeroWinsEveryTrial() {

        /*
         * Hero owns A♠.
         *
         * Board contains:
         * K♠ Q♠ J♠ T♠ 3♦
         *
         * Hero therefore has a guaranteed royal flush.
         * Since A♠ is removed from the deck,
         * no opponent can also make that royal flush.
         */

        List<Card> heroCards = List.of(
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.TWO,
                        Card.Suit.CLUB
                )
        );

        List<Card> board = List.of(
                new Card(
                        Card.Rank.KING,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.QUEEN,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.JACK,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.TEN,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.THREE,
                        Card.Suit.DIAMOND
                )
        );

        int trials = 100;

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        heroCards,
                        board,
                        trials,
                        2
                );

        MonteCarloResults results =
                service.runSimulation(scenario);

        assertEquals(trials, results.getTotalTrials());

        assertEquals(
                trials,
                results.getNumWins()
        );

        assertEquals(
                0,
                results.getNumLosses()
        );

        assertEquals(
                0,
                results.getNumTies()
        );

        assertEquals(
                trials,
                results
                        .getHeroHandRankDistribution()
                        .get(HandRank.ROYAL_FLUSH)
        );
    }

    @Test
    void runSimulation_royalFlushOnBoardEveryTrialIsTie() {

        /*
         * The board itself is a royal flush.
         *
         * Every player uses the exact same five-card hand,
         * therefore hero must tie every trial.
         */

        List<Card> heroCards = List.of(
                new Card(
                        Card.Rank.TWO,
                        Card.Suit.CLUB
                ),
                new Card(
                        Card.Rank.THREE,
                        Card.Suit.DIAMOND
                )
        );

        List<Card> board = List.of(
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.KING,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.QUEEN,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.JACK,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.TEN,
                        Card.Suit.SPADE
                )
        );

        int trials = 50;

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        heroCards,
                        board,
                        trials,
                        3
                );

        MonteCarloResults results =
                service.runSimulation(scenario);

        assertEquals(
                0,
                results.getNumWins()
        );

        assertEquals(
                0,
                results.getNumLosses()
        );

        assertEquals(
                trials,
                results.getNumTies()
        );

        assertEquals(
                trials,
                results
                        .getHeroHandRankDistribution()
                        .get(HandRank.ROYAL_FLUSH)
        );
    }

    @Test
    void runSimulation_classifiesEveryTrial() {

        int trials = 200;

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        validHeroCards(),
                        List.of(),
                        trials,
                        2
                );

        MonteCarloResults results =
                service.runSimulation(scenario);

        int classifiedTrials =
                results.getNumWins()
                        + results.getNumLosses()
                        + results.getNumTies();

        assertEquals(
                trials,
                classifiedTrials
        );
    }

    @Test
    void runSimulation_assignsExactlyOneHandRankPerTrial() {

        int trials = 200;

        MonteCarloScenario scenario =
                new MonteCarloScenario(
                        validHeroCards(),
                        List.of(),
                        trials,
                        2
                );

        MonteCarloResults results =
                service.runSimulation(scenario);

        int handRankCount =
                results
                        .getHeroHandRankDistribution()
                        .values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .sum();

        assertEquals(
                trials,
                handRankCount
        );
    }

    private List<Card> validHeroCards() {
        return List.of(
                new Card(
                        Card.Rank.ACE,
                        Card.Suit.SPADE
                ),
                new Card(
                        Card.Rank.KING,
                        Card.Suit.HEART
                )
        );
    }
}