# Q04. Card Game: Highest Value Card

**Duration:** 15 minutes (candidates reported finishing in about 15) · **Language:** Java · **Source:** IDFC sample sheet; asked onsite, round 1, per two LeetCode posts

## Problem Statement

You are given a list of exactly 5 playing cards. Each card is a string in the format `"rank-suit"`:

- rank is one of `2 3 4 5 6 7 8 9 10 J Q K A`
- suit is one of `H` (Hearts), `D` (Diamonds), `C` (Clubs) or `S` (Spades)

Ordering:

- Ranks: `2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A`
- Suits: `Hearts < Diamonds < Clubs < Spades`
- **Rank is compared first**, then suit. For example, `5-H > 2-S` and `5-S > 5-H`.

Return the card with the highest value. If more than one card has the highest value, which can only happen with duplicate cards, return the **first** one.

## Function

```java
String highestCard(List<String> cards)
```

## Examples

```
highestCard(["5-H", "A-S", "K-D", "10-C", "J-H"])  -> "A-S"
highestCard(["2-H", "3-D", "4-C", "5-S", "6-H"])   -> "6-H"
highestCard(["A-H", "A-D", "K-C", "Q-S", "J-H"])   -> "A-D"   // same rank, D > H
highestCard(["10-H", "J-D", "Q-C", "K-S", "A-H"])  -> "A-H"   // rank beats suit
highestCard(["9-C", "10-H", "10-D", "2-S", "10-H"]) -> "10-D"
```

## Expected Design

- `enum Rank` (with a `symbol` and a value given by its order) and `enum Suit`, each with a `fromSymbol(String)` lookup.
- `record Card(Rank rank, Suit suit) implements Comparable<Card>`, or a `Comparator.comparing(Card::rank).thenComparing(Card::suit)`.
- `Card.parse("10-C")`. Split on `-`, not on `charAt(0)`, or `"10"` breaks.
- Make one pass and keep the best card, replacing it only on a **strictly greater** card. That's how "first one wins" works. O(n).

## Edge cases to discuss

`"10"` being two characters · lower case such as `"a-s"` · invalid input like `"1-H"`, `"A-X"` or `"AS"` · fewer or more than 5 cards · null elements · why `Collections.max` might not return the first of equal cards (check its contract and say so).

## Follow-ups

1. Return the hand sorted from highest to lowest card.
2. Classify the hand: pair, two pair, three of a kind, straight, flush, full house, four of a kind. Use counting maps.
3. Compare two players' hands and return the winner.
4. Let the suit order be configured per game.
