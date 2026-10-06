# Poker V2 — Equity & Range Calculator

Poker V2 is a full-stack poker equity and range calculator that uses a custom Java Monte Carlo simulation engine to estimate win, tie, and loss probabilities across user-defined hole cards, board states, opponent counts, and opponent ranges.

The long-term vision is to evolve this project into a poker analytics and coaching platform by adding real hand-history data, player action tracking, and behavioral modeling.

---

## Project Vision

Most poker calculators answer a narrow question:

> “What are my odds of winning this hand?”

Poker V2 starts there, but is designed to grow into a more useful decision-support tool.

The first goal is to build a reliable Monte Carlo simulation engine that can calculate equity based on:

- Hero hole cards
- Known or unknown community cards
- Number of opponents
- Known opponent cards
- Opponent hand ranges
- Number of simulation iterations

Later, the project will expand from raw probability calculation into poker study and analytics by incorporating:

- Real hand-history data
- Player action tracking
- Player profile statistics
- Behavioral modeling
- Leak detection
- Coaching-style recommendations

---

## Current Core Goal

The current development focus is:

> Build a trustworthy Java simulation engine and database-backed equity calculator before expanding into player behavior modeling.

This means the current priority is not to build a complete AI poker coach yet. Instead, the project is being developed in stages:

1. Finish and test the poker hand evaluation engine.
2. Store simulation results in PostgreSQL.
3. Build a Monte Carlo equity calculator.
4. Add support for known board cards and opponent counts.
5. Add opponent range modeling.
6. Build a full-stack web interface.
7. Later, add real-world hand-history and behavioral analytics.

---

## Current Features

### Poker Engine

The current Java engine supports:

- Card, Deck, and Player models
- Texas Hold’em hand evaluation
- Best 5-card hand extraction from 7 available cards
- Winner detection across multiple players
- Tie handling
- Hand ranking support:
   - High Card
   - Pair
   - Two Pair
   - Three of a Kind
   - Straight
   - Flush
   - Full House
   - Four of a Kind
   - Straight Flush
   - Royal Flush

### HandResult Architecture

The engine is being refactored around a `HandResult` object.

Each evaluated player hand stores:

- `HandRank`
- `handStrength`
- `bestFiveCards`

The `bestFiveCards` list is ordered in comparison-ready order.

Examples:

```text
PAIR:
[pair, pair, highest kicker, second kicker, third kicker]

TWO_PAIR:
[higher pair, higher pair, lower pair, lower pair, kicker]

FULL_HOUSE:
[trips, trips, trips, pair, pair]

STRAIGHT:
[highest card, ..., lowest card]

ACE-LOW STRAIGHT:
[5, 4, 3, 2, A]