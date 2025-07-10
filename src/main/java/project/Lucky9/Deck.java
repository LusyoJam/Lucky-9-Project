package project.Lucky9;

import java.util.*;

public class Deck {
    private final List<Card> deckOfCards;
    private final Random randomizer;

    public Deck() {
        deckOfCards = new ArrayList<>();
        randomizer = new Random();
        makeDeck();
    }

    public Card distributeCards() {
        int randomCard = randomizer.nextInt(deckOfCards.size());
        Card cardToGive = deckOfCards.get(randomCard);
        deckOfCards.remove(randomCard);
        return cardToGive;
    }

    public void makeDeck() {
        for (int numberOfDecks = 0; numberOfDecks < 2; numberOfDecks++) {
            for (Card.Suit cardSuits : Card.Suit.values()) {
                for (Card.Rank cardRanks : Card.Rank.values()) {
                    deckOfCards.add(new Card(cardSuits, cardRanks));
                }
            }
        }
        Collections.shuffle(deckOfCards);
    }

    public static final class Card {
        private final Suit suit;
        private final Rank rank;
        private final int value;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
            this.value = rank.getRankValue();
        }

        public Suit getSuit() {
            return suit;
        }

        public int getValue() {
            return value;
        }

        public Rank getRank() {
            return rank;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Card card)) return false;
            return value == card.value && suit == card.suit && rank == card.rank;
        }

        @Override
        public int hashCode() {
            return Objects.hash(suit, rank, value);
        }

        @Override
        public String toString() {
            return "|" + getRank() + " " + getSuit() + "|";
        }

        public enum Rank {
            ACE(1, "A"),
            TWO(2, "2"),
            THREE(3, "3"),
            FOUR(4, "4"),
            FIVE(5, "5"),
            SIX(6, "6"),
            SEVEN(7, "7"),
            EIGHT(8, "8"),
            NINE(9, "9"),
            TEN(10, "10"),
            KING(0, "K"),
            QUEEN(0, "Q"),
            JACK(0, "J");

            private final int rankValue;
            private final String rankSymbol;

            Rank(int rankValue, String rankSymbol) {
                this.rankValue = rankValue;
                this.rankSymbol = rankSymbol;
            }

            public int getRankValue() {
                return this.rankValue;
            }

            public String getRankSymbol() {
                return rankSymbol;
            }

            @Override
            public String toString() {
                return getRankSymbol();
            }
        }

        public enum Suit {
            HEART('♥'),
            DIAMOND('♦'),
            CLUB('♣'),
            SPADE('♠');

            private final char symbol;

            Suit(char symbol) {
                this.symbol = symbol;
            }

            public char getSymbol() {
                return symbol;
            }

            @Override
            public String toString() {
                return Character.toString(getSymbol());
            }
        }
    }
}
