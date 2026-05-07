package test;

import main.model.*;
import main.simulation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PokerSimulationTest {
    PokerSimulation simulation = new PokerSimulation();

    @Test
    @DisplayName("Test tie breaker case between two players with same value hand")
    void checkTieBreakerBetweenTwoPlayers() {
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.ACE, Card.Suit.HEART),
                        card(Card.Rank.TWO, Card.Suit.SPADE)
                )),
                new Player("player2", cards(
                        card(Card.Rank.ACE, Card.Suit.SPADE),
                        card(Card.Rank.TWO, Card.Suit.CLUB)
                ))
        )));
        simulation.setCommunityCards(cards(
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.SIX, Card.Suit.SPADE),
                card(Card.Rank.SEVEN, Card.Suit.CLUB),
                card(Card.Rank.EIGHT, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.DIAMOND)
        ));

        simulation.handleWinners();

        assertEquals(2, simulation.getWinningPlayers().size());
    }

    @Test
    @DisplayName("Test high card between two players with same value hand")
    void checkHighCardBetweenTwoPlayers() {
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.ACE, Card.Suit.HEART),
                        card(Card.Rank.TWO, Card.Suit.SPADE)
                )),
                new Player("player2", cards(
                        card(Card.Rank.QUEEN, Card.Suit.SPADE),
                        card(Card.Rank.TWO, Card.Suit.CLUB)
                ))
        )));
        simulation.setCommunityCards(cards(
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.SIX, Card.Suit.SPADE),
                card(Card.Rank.SEVEN, Card.Suit.CLUB),
                card(Card.Rank.EIGHT, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.DIAMOND)
        ));

        simulation.handleWinners();

        Player winner = simulation.getWinningPlayers().get(0);
        assertEquals(2, winner.getPlayerResults().convertHandRankToNum());
    }

    @Test
    @DisplayName("Fixing false flush logic bug")
    void checkFalseFlush() {
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.EIGHT, Card.Suit.HEART),
                        card(Card.Rank.KING, Card.Suit.DIAMOND)
                )),
                new Player("player2", cards(
                        card(Card.Rank.FIVE, Card.Suit.SPADE),
                        card(Card.Rank.SIX, Card.Suit.DIAMOND)
                ))
        )));
        simulation.setCommunityCards(cards(
                card(Card.Rank.TEN, Card.Suit.CLUB),
                card(Card.Rank.THREE, Card.Suit.HEART),
                card(Card.Rank.ACE, Card.Suit.SPADE),
                card(Card.Rank.TEN, Card.Suit.DIAMOND),
                card(Card.Rank.ACE, Card.Suit.HEART)
        ));

        simulation.handleWinners();

        Player winner = getOnlyWinner(simulation);
        assertEquals(3, winner.getPlayerResults().convertHandRankToNum());
    }

    @Test
    @DisplayName("Player with quads beats full house")
    void quadsBeatFullHouse() {
        simulation.setCommunityCards(cards(
                card(Card.Rank.NINE, Card.Suit.CLUB),
                card(Card.Rank.NINE, Card.Suit.HEART),
                card(Card.Rank.NINE, Card.Suit.SPADE),
                card(Card.Rank.ACE, Card.Suit.DIAMOND),
                card(Card.Rank.ACE, Card.Suit.HEART)
        ));
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.NINE, Card.Suit.DIAMOND),
                        card(Card.Rank.KING, Card.Suit.DIAMOND)
                )),
                new Player("player2", cards(
                        card(Card.Rank.FIVE, Card.Suit.SPADE),
                        card(Card.Rank.SIX, Card.Suit.DIAMOND)
                ))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        assertEquals(cards(
                card(Card.Rank.NINE, Card.Suit.DIAMOND),
                card(Card.Rank.KING, Card.Suit.DIAMOND)
        ), winner.getHoleCards(), "Quads player should win over the board full house");
    }

    @Test
    @DisplayName("Player with higher flush beats flush on the board")
    void higherFlushBeatsBoardFlush() {
        simulation.setCommunityCards(cards(
                card(Card.Rank.TWO, Card.Suit.CLUB),
                card(Card.Rank.FIVE, Card.Suit.CLUB),
                card(Card.Rank.EIGHT, Card.Suit.CLUB),
                card(Card.Rank.JACK, Card.Suit.CLUB),
                card(Card.Rank.QUEEN, Card.Suit.CLUB)
        ));
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.ACE, Card.Suit.CLUB),
                        card(Card.Rank.TWO, Card.Suit.HEART)
                )),
                new Player("player2", cards(
                        card(Card.Rank.KING, Card.Suit.DIAMOND),
                        card(Card.Rank.THREE, Card.Suit.SPADE)
                ))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        assertEquals(cards(
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.HEART)
        ), winner.getHoleCards(), "A-high flush should beat board Q-high flush");
    }

    @Test
    @DisplayName("Player with higher straight beats straight on the board")
    void higherStraightBeatsBoardStraight() {
        simulation.setCommunityCards(cards(
                card(Card.Rank.FIVE, Card.Suit.DIAMOND),
                card(Card.Rank.SIX, Card.Suit.CLUB),
                card(Card.Rank.SEVEN, Card.Suit.HEART),
                card(Card.Rank.EIGHT, Card.Suit.SPADE),
                card(Card.Rank.NINE, Card.Suit.DIAMOND)
        ));
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.TEN, Card.Suit.CLUB),
                        card(Card.Rank.TWO, Card.Suit.HEART)
                )),
                new Player("player2", cards(
                        card(Card.Rank.TWO, Card.Suit.SPADE),
                        card(Card.Rank.THREE, Card.Suit.SPADE)
                ))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        assertEquals(cards(
                card(Card.Rank.TEN, Card.Suit.CLUB),
                card(Card.Rank.TWO, Card.Suit.HEART)
        ), winner.getHoleCards(), "10-high straight should beat board 9-high straight");
    }

    @Test
    @DisplayName("Exact tie when players use the same board bestFiveCards")
    void boardStraightExactTie() {
        PokerSimulation game = runSimulation(
                cards(
                        card(Card.Rank.ACE, Card.Suit.CLUB),
                        card(Card.Rank.KING, Card.Suit.DIAMOND),
                        card(Card.Rank.QUEEN, Card.Suit.HEART),
                        card(Card.Rank.JACK, Card.Suit.SPADE),
                        card(Card.Rank.TEN, Card.Suit.CLUB)
                ),
                player("player1", card(Card.Rank.TWO, Card.Suit.HEART), card(Card.Rank.THREE, Card.Suit.CLUB)),
                player("player2", card(Card.Rank.FOUR, Card.Suit.SPADE), card(Card.Rank.FIVE, Card.Suit.DIAMOND)),
                player("player3", card(Card.Rank.SEVEN, Card.Suit.HEART), card(Card.Rank.NINE, Card.Suit.DIAMOND))
        );

        assertEquals(3, game.getWinningPlayers().size(),
                "All players should tie using the same board bestFiveCards");
        for (Player p : game.getPlayers()) {
            assertHand(HandRank.STRAIGHT, p.getPlayerResults(),
                    Card.Rank.ACE, Card.Rank.KING, Card.Rank.QUEEN, Card.Rank.JACK, Card.Rank.TEN);
        }
    }

    @Test
    @DisplayName("Straight flush beats normal flush")
    void straightFlushBeatsFlush() {
        simulation.setCommunityCards(cards(
                card(Card.Rank.TWO, Card.Suit.SPADE),
                card(Card.Rank.THREE, Card.Suit.SPADE),
                card(Card.Rank.FOUR, Card.Suit.SPADE),
                card(Card.Rank.NINE, Card.Suit.DIAMOND),
                card(Card.Rank.KING, Card.Suit.CLUB)
        ));
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.FIVE, Card.Suit.SPADE),
                        card(Card.Rank.SIX, Card.Suit.SPADE)
                )),
                new Player("player2", cards(
                        card(Card.Rank.ACE, Card.Suit.SPADE),
                        card(Card.Rank.TWO, Card.Suit.HEART)
                ))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        assertEquals(cards(
                card(Card.Rank.FIVE, Card.Suit.SPADE),
                card(Card.Rank.SIX, Card.Suit.SPADE)
        ), winner.getHoleCards(), "Straight flush should beat normal flush");
    }

    @Test
    @DisplayName("Royal flush beats lower straight flush")
    void royalFlushBeatsLowerStraightFlush() {
        simulation.setCommunityCards(cards(
                card(Card.Rank.TEN, Card.Suit.HEART),
                card(Card.Rank.JACK, Card.Suit.HEART),
                card(Card.Rank.QUEEN, Card.Suit.HEART),
                card(Card.Rank.KING, Card.Suit.HEART),
                card(Card.Rank.THREE, Card.Suit.CLUB)
        ));
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.ACE, Card.Suit.HEART),
                        card(Card.Rank.TWO, Card.Suit.SPADE)
                )),
                new Player("player2", cards(
                        card(Card.Rank.NINE, Card.Suit.HEART),
                        card(Card.Rank.FOUR, Card.Suit.DIAMOND)
                ))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        assertEquals(cards(
                card(Card.Rank.ACE, Card.Suit.HEART),
                card(Card.Rank.TWO, Card.Suit.SPADE)
        ), winner.getHoleCards(), "Royal flush should beat lower straight flush");
    }

    @Test
    @DisplayName("High card test")
    void highCardTieBreaker() {
        simulation.setCommunityCards(cards(
                card(Card.Rank.ACE, Card.Suit.CLUB),
                card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                card(Card.Rank.NINE, Card.Suit.SPADE),
                card(Card.Rank.SIX, Card.Suit.HEART),
                card(Card.Rank.THREE, Card.Suit.CLUB)
        ));
        simulation.setPlayers(new ArrayList<Player>(Arrays.asList(
                new Player("player1", cards(
                        card(Card.Rank.KING, Card.Suit.SPADE),
                        card(Card.Rank.TEN, Card.Suit.DIAMOND)
                )),
                new Player("player2", cards(
                        card(Card.Rank.KING, Card.Suit.DIAMOND),
                        card(Card.Rank.EIGHT, Card.Suit.SPADE)
                ))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        assertEquals(cards(
                card(Card.Rank.KING, Card.Suit.SPADE),
                card(Card.Rank.TEN, Card.Suit.DIAMOND)
        ), winner.getHoleCards(), "player1 should beat player2 with K and 10");
    }

    @Test
    @DisplayName("PokerSimulation uses bestFiveCards for same-rank tie breakers")
    void handleWinnersUsesBestFiveCardsForSameRankTieBreakers() {
        PokerSimulation pairGame = runSimulation(
                cards(
                        card(Card.Rank.ACE, Card.Suit.CLUB),
                        card(Card.Rank.KING, Card.Suit.CLUB),
                        card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                        card(Card.Rank.SIX, Card.Suit.HEART),
                        card(Card.Rank.TWO, Card.Suit.CLUB)
                ),
                player("player1", card(Card.Rank.ACE, Card.Suit.SPADE), card(Card.Rank.TEN, Card.Suit.DIAMOND)),
                player("player2", card(Card.Rank.ACE, Card.Suit.HEART), card(Card.Rank.NINE, Card.Suit.DIAMOND))
        );
        assertOnlyWinner(pairGame, "player1");

        PokerSimulation twoPairGame = runSimulation(
                cards(
                        card(Card.Rank.KING, Card.Suit.CLUB),
                        card(Card.Rank.KING, Card.Suit.DIAMOND),
                        card(Card.Rank.NINE, Card.Suit.CLUB),
                        card(Card.Rank.NINE, Card.Suit.DIAMOND),
                        card(Card.Rank.TWO, Card.Suit.CLUB)
                ),
                player("player1", card(Card.Rank.ACE, Card.Suit.SPADE), card(Card.Rank.SEVEN, Card.Suit.DIAMOND)),
                player("player2", card(Card.Rank.QUEEN, Card.Suit.SPADE), card(Card.Rank.SEVEN, Card.Suit.HEART))
        );
        assertOnlyWinner(twoPairGame, "player1");

        PokerSimulation flushGame = runSimulation(
                cards(
                        card(Card.Rank.JACK, Card.Suit.HEART),
                        card(Card.Rank.NINE, Card.Suit.HEART),
                        card(Card.Rank.SIX, Card.Suit.HEART),
                        card(Card.Rank.THREE, Card.Suit.HEART),
                        card(Card.Rank.TWO, Card.Suit.CLUB)
                ),
                player("player1", card(Card.Rank.ACE, Card.Suit.HEART), card(Card.Rank.KING, Card.Suit.CLUB)),
                player("player2", card(Card.Rank.QUEEN, Card.Suit.HEART), card(Card.Rank.KING, Card.Suit.DIAMOND))
        );
        assertOnlyWinner(flushGame, "player1");

        PokerSimulation straightGame = runSimulation(
                cards(
                        card(Card.Rank.FIVE, Card.Suit.CLUB),
                        card(Card.Rank.FOUR, Card.Suit.DIAMOND),
                        card(Card.Rank.THREE, Card.Suit.SPADE),
                        card(Card.Rank.TWO, Card.Suit.HEART),
                        card(Card.Rank.KING, Card.Suit.CLUB)
                ),
                player("player1", card(Card.Rank.SIX, Card.Suit.CLUB), card(Card.Rank.ACE, Card.Suit.DIAMOND)),
                player("player2", card(Card.Rank.ACE, Card.Suit.SPADE), card(Card.Rank.NINE, Card.Suit.DIAMOND))
        );
        assertOnlyWinner(straightGame, "player1");

        PokerSimulation quadsGame = runSimulation(
                cards(
                        card(Card.Rank.EIGHT, Card.Suit.SPADE),
                        card(Card.Rank.EIGHT, Card.Suit.HEART),
                        card(Card.Rank.EIGHT, Card.Suit.CLUB),
                        card(Card.Rank.EIGHT, Card.Suit.DIAMOND),
                        card(Card.Rank.TWO, Card.Suit.CLUB)
                ),
                player("player1", card(Card.Rank.ACE, Card.Suit.SPADE), card(Card.Rank.THREE, Card.Suit.CLUB)),
                player("player2", card(Card.Rank.KING, Card.Suit.SPADE), card(Card.Rank.QUEEN, Card.Suit.CLUB))
        );
        assertOnlyWinner(quadsGame, "player1");
    }

    private Card card(Card.Rank rank, Card.Suit suit) {
        return new Card(rank, suit);
    }

    private ArrayList<Card> cards(Card... cards) {
        return new ArrayList<>(Arrays.asList(cards));
    }

    private Player player(String name, Card... holeCards) {
        return new Player(name, cards(holeCards));
    }

    private PokerSimulation runSimulation(ArrayList<Card> communityCards, Player... players) {
        PokerSimulation game = new PokerSimulation();
        game.setCommunityCards(communityCards);
        game.setPlayers(new ArrayList<>(Arrays.asList(players)));
        game.handleWinners();
        return game;
    }

    private Player getOnlyWinner(PokerSimulation simulation) {
        var winners = simulation.getWinningPlayers();
        assertEquals(1, winners.size(), "should be one winner");
        return winners.get(0);
    }

    private void assertHand(HandRank expectedRank, HandResult result, Card.Rank... expectedRanks) {
        assertEquals(expectedRank, result.getRank());
        ArrayList<Card> best = result.getBestFiveCards();
        assertEquals(5, best.size(), "bestFiveCards should always contain five cards");

        for (int i = 0; i < expectedRanks.length; i++) {
            assertEquals(expectedRanks[i], best.get(i).getRank(), "rank mismatch at bestFiveCards index " + i);
        }
    }

    private void assertOnlyWinner(PokerSimulation game, String expectedWinnerName) {
        assertEquals(expectedWinnerName, getOnlyWinner(game).getName());
    }
}
