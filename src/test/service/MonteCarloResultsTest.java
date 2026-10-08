package test.service;

import main.service.MonteCarloResults;
import main.simulation.HandRank;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MonteCarloResultsTest {

    @Test
    void constructor_storesAggregateValues() {

        Map<HandRank, Integer> distribution =
                new EnumMap<>(HandRank.class);

        distribution.put(HandRank.HIGH_CARD, 20);
        distribution.put(HandRank.PAIR, 50);
        distribution.put(HandRank.TWO_PAIR, 30);

        MonteCarloResults results =
                new MonteCarloResults(
                        100,
                        40,
                        50,
                        10,
                        distribution
                );

        assertEquals(100, results.getTotalTrials());
        assertEquals(40, results.getNumWins());
        assertEquals(50, results.getNumLosses());
        assertEquals(10, results.getNumTies());

        assertEquals(
                50,
                results.getHeroHandRankDistribution()
                        .get(HandRank.PAIR)
        );
    }

    @Test
    void constructor_defensivelyCopiesDistribution() {

        Map<HandRank, Integer> distribution =
                new EnumMap<>(HandRank.class);

        distribution.put(HandRank.PAIR, 25);

        MonteCarloResults results =
                new MonteCarloResults(
                        25,
                        10,
                        10,
                        5,
                        distribution
                );

        distribution.put(HandRank.PAIR, 999);

        assertEquals(
                25,
                results.getHeroHandRankDistribution()
                        .get(HandRank.PAIR)
        );
    }

    @Test
    void distribution_cannotBeModifiedThroughGetter() {

        Map<HandRank, Integer> distribution =
                new EnumMap<>(HandRank.class);

        distribution.put(HandRank.PAIR, 10);

        MonteCarloResults results =
                new MonteCarloResults(
                        10,
                        4,
                        5,
                        1,
                        distribution
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> results
                        .getHeroHandRankDistribution()
                        .put(HandRank.FLUSH, 100)
        );
    }
}