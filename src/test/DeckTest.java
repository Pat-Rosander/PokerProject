package test;

import main.model.*;
import main.simulation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck deck = new Deck();
    PokerSimulation simulation = new PokerSimulation();

    @Test
    @DisplayName("Size test after a shuffle")
    void shuffleSize() {
        deck = simulation.getDeck();
        deck.shuffle();
        assertEquals(52, deck.size());
    }
    @Test
    @DisplayName("main.model.Deck contains all cards test")
    void deckContainsAll() {
        deck = simulation.getDeck();
        deck.shuffle();
        assertEquals(true, deck.containsAllCards());
    }
    @Test
    @DisplayName("main.simulation.PokerSimulation playerSetup method")
    void playerSetup() {
        // Add 3 players to simulation
        simulation.addPlayer("player1");
        simulation.addPlayer("player2");
        simulation.addPlayer("player3");
        deck = simulation.getDeck();
        // Each player dealt holeCards upon initialization, deck size should be 46.
        assertEquals(46, deck.size());
        Player player1 = simulation.getPlayers().get(0);
        assertEquals(2, player1.getHoleCards().size());
    }
    @Test
    @DisplayName("Player getHoleCards test")
    void getHoleCards() {
        simulation.addPlayer("player1");
        simulation.addPlayer("player2");
        simulation.addPlayer("player3");

        Player player1 = simulation.getPlayers().get(0);
        assertEquals(2, player1.getHoleCards().size());
    }
    @Test
    @DisplayName("Check that players holeCards are not included in deck")
    void checkIfHoleCardsInDeck() {
        simulation.addPlayer("player1");
        simulation.addPlayer("player2");
        simulation.addPlayer("player3");
        deck = simulation.getDeck();

        for (int i = 0; i < simulation.getPlayers().size(); i++) {
            ArrayList<Card> tempPlayerHoleCard = simulation.getPlayers().get(i).getHoleCards();
            for (int j = 0; j < tempPlayerHoleCard.size(); j++) {
                assertEquals(false, deck.containsCard(tempPlayerHoleCard.get(j)));
            }
        }
    }
    @Test
    @DisplayName("Test tie breaker case between two players with same value hand")
    void checkTieBreakerBetweenTwoPlayers() {
        // Add two players to simulation with exact same value holeCards
        // p1 --> Ace of Hearts and Two of Spades
        // p2 --> Ace of Spades and Two of Clubs
        simulation.setPlayers(
                new ArrayList<Player>(Arrays.asList(
                        new Player("player1", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.ACE, Card.Suit.HEART),
                                new Card(Card.Rank.TWO, Card.Suit.SPADE)
                        ))
                        ),
                        new Player("player2", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                                new Card(Card.Rank.TWO, Card.Suit.CLUB)
                        ))
                        )
                ))
        );
        // Cards on table for tie-breaker case
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.SIX, Card.Suit.SPADE),
                new Card(Card.Rank.SEVEN, Card.Suit.CLUB),
                new Card(Card.Rank.EIGHT, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND)
        ));
        simulation.setCommunityCards(communityCards);
        simulation.handleWinners();
        // Two players with same value hand, and same value high card; therefore must be a tie
        assertEquals(2, simulation.getWinningPlayers().size());
    }
    @Test
    @DisplayName("Test high card between two players with same value hand")
    void checkHighCardBetweenTwoPlayers() {
        // Add two players to simulation with same value hand and test winner logic based on higher card
        // p1 --> Ace of Hearts and Two of Spades
        // p2 --> Queen of Spades and Two of Clubs
        simulation.setPlayers(
                new ArrayList<Player>(Arrays.asList(
                        new Player("player1", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.ACE, Card.Suit.HEART),
                                new Card(Card.Rank.TWO, Card.Suit.SPADE)
                        ))
                        ),
                        new Player("player2", new ArrayList<Card>(Arrays.asList(
                                new Card(Card.Rank.QUEEN, Card.Suit.SPADE),
                                new Card(Card.Rank.TWO, Card.Suit.CLUB)
                        ))
                        )
                ))
        );
        // Cards on table for tie-breaker case
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.SIX, Card.Suit.SPADE),
                new Card(Card.Rank.SEVEN, Card.Suit.CLUB),
                new Card(Card.Rank.EIGHT, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND)
        ));
        simulation.setCommunityCards(communityCards);
        simulation.handleWinners();

        Player winner = simulation.getWinningPlayers().get(0);
        assertEquals(2, winner.getPlayerResults().convertHandRankToNum());

    }
    @Test
    @DisplayName("Fixing false flush logic bug")
    void checkFalseFlush() {
        simulation.setPlayers
                (new ArrayList<Player>(Arrays.asList(
                                new Player("player1", new ArrayList<Card>(Arrays.asList(
                                        new Card(Card.Rank.EIGHT, Card.Suit.HEART),
                                        new Card(Card.Rank.KING, Card.Suit.DIAMOND)
                                ))
                                ),
                                new Player("player2", new ArrayList<Card>(Arrays.asList(
                                        new Card(Card.Rank.FIVE, Card.Suit.SPADE),
                                        new Card(Card.Rank.SIX, Card.Suit.DIAMOND)
                                ))
                                )
                        ))
                );
        // Cards on table for tie-breaker case
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.TEN, Card.Suit.CLUB),
                new Card(Card.Rank.THREE, Card.Suit.HEART),
                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                new Card(Card.Rank.TEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.ACE, Card.Suit.HEART)
        ));
        simulation.setCommunityCards(communityCards);
        simulation.handleWinners();

        Player winner = getOnlyWinner(simulation);

        assertEquals(3, winner.getPlayerResults().convertHandRankToNum());
    }
    @Test
    @DisplayName("Player with quads beats full house")
    void quadsBeatFullHouse() {
        // Full house on board
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.NINE, Card.Suit.CLUB),
                new Card(Card.Rank.NINE, Card.Suit.HEART),
                new Card(Card.Rank.NINE, Card.Suit.SPADE),
                new Card(Card.Rank.ACE, Card.Suit.DIAMOND),
                new Card(Card.Rank.ACE, Card.Suit.HEART)
        ));
        simulation.setCommunityCards(communityCards);

        // P1 has quad 9's and P2 has full house
        simulation.setPlayers
                (new ArrayList<Player>(Arrays.asList(
                                new Player("player1", new ArrayList<Card>(Arrays.asList(
                                        new Card(Card.Rank.NINE, Card.Suit.DIAMOND),
                                        new Card(Card.Rank.KING, Card.Suit.DIAMOND)
                                ))
                                ),
                                new Player("player2", new ArrayList<Card>(Arrays.asList(
                                        new Card(Card.Rank.FIVE, Card.Suit.SPADE),
                                        new Card(Card.Rank.SIX, Card.Suit.DIAMOND)
                                ))
                                )
                        ))
                );

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        ArrayList<Card> expectedHole = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.NINE, Card.Suit.DIAMOND),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND)
        ));
        assertEquals(expectedHole, winner.getHoleCards(), "Quads player should win over the board full house");
    }
    @Test
    @DisplayName("Player with higher flush beats flush on the board")
    void higherFlushBeatsBoardFlush() {
        // Board: 2♣ 5♣ 8♣ J♣ Q♣ -> Q-high club flush
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.TWO, Card.Suit.CLUB),
                new Card(Card.Rank.FIVE, Card.Suit.CLUB),
                new Card(Card.Rank.EIGHT, Card.Suit.CLUB),
                new Card(Card.Rank.JACK, Card.Suit.CLUB),
                new Card(Card.Rank.QUEEN, Card.Suit.CLUB)
        ));
        simulation.setCommunityCards(communityCards);

        // p1: A♣ x -> A-high flush (A,Q,J,8,5)
        // p2: K♦ x -> just the board's Q-high flush
        simulation.setPlayers(new ArrayList<>(Arrays.asList(
                new Player("player1", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.ACE, Card.Suit.CLUB),
                        new Card(Card.Rank.TWO, Card.Suit.HEART)
                ))),
                new Player("player2", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.KING, Card.Suit.DIAMOND),
                        new Card(Card.Rank.THREE, Card.Suit.SPADE)
                )))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        ArrayList<Card> expectedHole = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.CLUB),
                new Card(Card.Rank.TWO, Card.Suit.HEART)
        ));
        assertEquals(expectedHole, winner.getHoleCards(), "A-high flush should beat board Q-high flush");
    }
    @Test
    @DisplayName("Player with higher straight beats straight on the board")
    void higherStraightBeatsBoardStraight() {
        // Board: 5♦ 6♣ 7♥ 8♠ 9♦ -> 9-high straight on board
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.FIVE, Card.Suit.DIAMOND),
                new Card(Card.Rank.SIX, Card.Suit.CLUB),
                new Card(Card.Rank.SEVEN, Card.Suit.HEART),
                new Card(Card.Rank.EIGHT, Card.Suit.SPADE),
                new Card(Card.Rank.NINE, Card.Suit.DIAMOND)
        ));
        simulation.setCommunityCards(communityCards);

        // p1: T♣ X -> 10-high straight (6-7-8-9-T)
        // p2: 2♠ 3♠ -> only the board 9-high straight
        simulation.setPlayers(new ArrayList<>(Arrays.asList(
                new Player("player1", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.TEN, Card.Suit.CLUB),
                        new Card(Card.Rank.TWO, Card.Suit.HEART)
                ))),
                new Player("player2", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.TWO, Card.Suit.SPADE),
                        new Card(Card.Rank.THREE, Card.Suit.SPADE)
                )))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        ArrayList<Card> expectedHole = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.TEN, Card.Suit.CLUB),
                new Card(Card.Rank.TWO, Card.Suit.HEART)
        ));
        assertEquals(expectedHole, winner.getHoleCards(), "10-high straight should beat board 9-high straight");
    }
    @Test
    @DisplayName("Exact tie when both players use the board straight")
    void boardStraightExactTie() {
        // Board: A♣ K♦ Q♥ J♠ T♣ -> Broadway straight on board
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.CLUB),
                new Card(Card.Rank.KING, Card.Suit.DIAMOND),
                new Card(Card.Rank.QUEEN, Card.Suit.HEART),
                new Card(Card.Rank.JACK, Card.Suit.SPADE),
                new Card(Card.Rank.TEN, Card.Suit.CLUB)
        ));
        simulation.setCommunityCards(communityCards);

        // Both players have random junk; best hand is the board for both
        simulation.setPlayers(new ArrayList<>(Arrays.asList(
                new Player("player1", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.TWO, Card.Suit.HEART),
                        new Card(Card.Rank.THREE, Card.Suit.CLUB)
                ))),
                new Player("player2", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.FOUR, Card.Suit.SPADE),
                        new Card(Card.Rank.FIVE, Card.Suit.DIAMOND)
                )))
        )));

        simulation.handleWinners();

        for (Player p : simulation.getPlayers()) {
            HandResult r = p.getPlayerResults();
            System.out.println(p.getName() + " rank=" + r.getRank() + " strength=" + r.getHandStrength());
            System.out.println("best=" + r.getBestFiveCards());
        }
        System.out.println("winners=" + simulation.getWinningPlayers());

        assertEquals(2, simulation.getWinningPlayers().size(),
                "Both players should tie using the board straight");
    }
    @Test
    @DisplayName("Straight flush beats normal flush")
    void straightFlushBeatsFlush() {
        // Board: 2♠ 3♠ 4♠ 9♦ K♣
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.TWO, Card.Suit.SPADE),
                new Card(Card.Rank.THREE, Card.Suit.SPADE),
                new Card(Card.Rank.FOUR, Card.Suit.SPADE),
                new Card(Card.Rank.NINE, Card.Suit.DIAMOND),
                new Card(Card.Rank.KING, Card.Suit.CLUB)
        ));
        simulation.setCommunityCards(communityCards);

        // p1: 5♠ 6♠ -> straight flush 2-6♠
        // p2: A♠ x -> just A-high flush in spades
        simulation.setPlayers(new ArrayList<>(Arrays.asList(
                new Player("player1", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.FIVE, Card.Suit.SPADE),
                        new Card(Card.Rank.SIX, Card.Suit.SPADE)
                ))),
                new Player("player2", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.ACE, Card.Suit.SPADE),
                        new Card(Card.Rank.TWO, Card.Suit.HEART)
                )))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        ArrayList<Card> expectedHole = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.FIVE, Card.Suit.SPADE),
                new Card(Card.Rank.SIX, Card.Suit.SPADE)
        ));
        assertEquals(expectedHole, winner.getHoleCards(), "Straight flush should beat normal flush");
    }
    @Test
    @DisplayName("Royal flush beats lower straight flush")
    void royalFlushBeatsLowerStraightFlush() {
        // Board: T♥ J♥ Q♥ K♥ 3♣
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.TEN, Card.Suit.HEART),
                new Card(Card.Rank.JACK, Card.Suit.HEART),
                new Card(Card.Rank.QUEEN, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.THREE, Card.Suit.CLUB)
        ));
        simulation.setCommunityCards(communityCards);

        // p1: A♥ x -> royal flush hearts (T-J-Q-K-A)
        // p2: 9♥ x -> 9-T-J-Q-K straight flush
        simulation.setPlayers(new ArrayList<>(Arrays.asList(
                new Player("player1", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.ACE, Card.Suit.HEART),
                        new Card(Card.Rank.TWO, Card.Suit.SPADE)
                ))),
                new Player("player2", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.NINE, Card.Suit.HEART),
                        new Card(Card.Rank.FOUR, Card.Suit.DIAMOND)
                )))
        )));

        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);

        ArrayList<Card> expectedHole = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.HEART),
                new Card(Card.Rank.TWO, Card.Suit.SPADE)
        ));
        assertEquals(expectedHole, winner.getHoleCards(), "Royal flush should beat lower straight flush");
    }
    @Test
    @DisplayName("High card test")
    void highCardTieBreaker() {
        // Board: A♣ Q♦ 9♠ 6♥ 3♣
        ArrayList<Card> communityCards = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.CLUB),
                new Card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.NINE, Card.Suit.SPADE),
                new Card(Card.Rank.SIX, Card.Suit.HEART),
                new Card(Card.Rank.THREE, Card.Suit.CLUB)
        ));
        simulation.setCommunityCards(communityCards);
        // p1: K♠ 10♦
        // p2: K♦ 8♠
        simulation.setPlayers(new ArrayList<>(Arrays.asList(
                new Player("player1", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.KING, Card.Suit.SPADE),
                        new Card(Card.Rank.TEN, Card.Suit.DIAMOND)
                ))),
                new Player("player2", new ArrayList<>(Arrays.asList(
                        new Card(Card.Rank.KING, Card.Suit.DIAMOND),
                        new Card(Card.Rank.EIGHT, Card.Suit.SPADE)
                )))
        )));
        simulation.handleWinners();
        Player winner = getOnlyWinner(simulation);
        ArrayList<Card> expectedHole = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.KING, Card.Suit.SPADE),
                new Card(Card.Rank.TEN, Card.Suit.DIAMOND)
        ));
        assertEquals(expectedHole, winner.getHoleCards(), "player1 should beat player2 with K♠ and 10♦");
    }

    @Test
    @DisplayName("Pair bestFiveCards are ordered pair first, then kickers descending")
    void pairBestFiveCardsOrder() {
        HandEvaluator eval = new HandEvaluator();

        ArrayList<Card> all7 = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                new Card(Card.Rank.ACE, Card.Suit.HEART),
                new Card(Card.Rank.KING, Card.Suit.SPADE),
                new Card(Card.Rank.QUEEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.TEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.SIX, Card.Suit.CLUB),
                new Card(Card.Rank.TWO, Card.Suit.SPADE)
        ));

        HandResult result = eval.evaluateHand(all7);

        assertEquals(HandRank.PAIR, result.getRank());

        ArrayList<Card> best = result.getBestFiveCards();

        assertEquals(Card.Rank.ACE, best.get(0).getRank());
        assertEquals(Card.Rank.ACE, best.get(1).getRank());
        assertEquals(Card.Rank.KING, best.get(2).getRank());
        assertEquals(Card.Rank.QUEEN, best.get(3).getRank());
        assertEquals(Card.Rank.TEN, best.get(4).getRank());
    }

    @Test
    @DisplayName("High card bestFiveCards are ordered descending values")
    void highCardBestFiveCardsOrder() {
        HandEvaluator eval = new HandEvaluator();

        ArrayList<Card> all7 = new ArrayList<>(Arrays.asList(
                new Card(Card.Rank.ACE, Card.Suit.SPADE),
                new Card(Card.Rank.KING, Card.Suit.HEART),
                new Card(Card.Rank.QUEEN, Card.Suit.SPADE),
                new Card(Card.Rank.TEN, Card.Suit.DIAMOND),
                new Card(Card.Rank.EIGHT, Card.Suit.DIAMOND),
                new Card(Card.Rank.SIX, Card.Suit.CLUB),
                new Card(Card.Rank.TWO, Card.Suit.SPADE)
        ));

        HandResult result = eval.evaluateHand(all7);

        assertEquals(HandRank.HIGH_CARD, result.getRank());

        ArrayList<Card> best = result.getBestFiveCards();

        assertEquals(Card.Rank.ACE, best.get(0).getRank());
        assertEquals(Card.Rank.KING, best.get(1).getRank());
        assertEquals(Card.Rank.QUEEN, best.get(2).getRank());
        assertEquals(Card.Rank.TEN, best.get(3).getRank());
        assertEquals(Card.Rank.EIGHT, best.get(4).getRank());
    }

    private Player getOnlyWinner(PokerSimulation simulation) {
        var winners = simulation.getWinningPlayers();
        assertEquals(1, winners.size(), "should be one winner");
        return winners.get(0);
    }
}