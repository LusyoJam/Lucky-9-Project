package project.Lucky9;

import java.util.List;
import java.util.function.Predicate;

public class PlayerController {
    private PlayerController(){} // to prevent
    // client from creating an object out of
    // this helper class.

    public static boolean isBalanceEnough(Player player) {
        return player.getBalance() >= 20;
    }

    public static boolean canTakeMoreCard(Player player) {
        return getTotalCardsValue(player) != 9;
    }

    public static boolean isDeckThreeCards(Player player) {
        return player.getPlayerHand().size() == 3;
    }

    public static int getCashHomeTakeOut(Player player) {
        if (player.getBalance() == player.getInitialFund()) {
            return player.getBalance();
        }
        return Math.max(player.getBalance() - player.getInitialFund(), 0);
    }

    public static int getTotalCardsValue(Player player) {
        int totalCardsValue = player.getPlayerHand()
                .stream()
                .mapToInt(Deck.Card::getValue)
                .sum();

        return (totalCardsValue >= 10) ? totalCardsValue % 10 : totalCardsValue;
    }

    public static String forToString(Player player) {
        StringBuilder descriptionMaker =
                new StringBuilder();
        List<Deck.Card> playerHand = player.getPlayerHand();

        descriptionMaker.append("1st card: ").append(playerHand.get(0)).append("\n")
                .append("2nd card: ").append(playerHand.get(1)).append("\n");

        if (isDeckThreeCards(player)) {
            descriptionMaker.append("3rd card: ").append(playerHand.get(2)).append("\n");
        }

        descriptionMaker.append(player.getPlayerName())
                .append(" total cards value: ")
                .append(player.getTotalCardsValue())
                .append("\n_______________\n");

        return descriptionMaker.toString();
    }

    public static Predicate <Player> isLucky9 (){
        return player -> player.getTotalCardsValue() == 9 && !(PlayerController.isDeckThreeCards(player));
    }
}
