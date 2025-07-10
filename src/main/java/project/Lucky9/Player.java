package project.Lucky9;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Deck.Card> playerHand;
    private final String playerName;
    private final int initialFund;
    protected int balance;
    private int betAmount;

    public Player(int initialFund, String playerName) {
        this.initialFund = initialFund;
        this.playerName = playerName;
        this.balance = initialFund;
        this.playerHand = new ArrayList<>();
    }

    public List<Deck.Card> getPlayerHand() {
        return playerHand;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public int getInitialFund() {
        return initialFund;
    }

    public int getBalance() {
        return balance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getTotalCardsValue() {
        return PlayerController.getTotalCardsValue(this);
    }

    public void addCard(Deck.Card card) {
        if (!PlayerController.isDeckThreeCards(this)) {
            playerHand.add(card);
        }
    }

    public void emptyPlayerHand() {
        playerHand.clear();
    }

    public void deductFromBalance() {
        balance -= betAmount;
    }

    public void addWinnings(int amount) {
        balance += amount;
    }

    public void showBet(int amount) {
        betAmount = amount;
        System.out.println(getPlayerName() + " bet: " + betAmount);
    }

    public void showBalance(){
        System.out.println(this.getPlayerName() + " balance: " +
                getBalance());
    }

    @Override
    public String toString() {
        return PlayerController.forToString(this);
    }
}

class Dealer extends Player {
    private int dealerCashTakeOut;

    public Dealer(int initialFund, String dealerName) {
        super(initialFund, dealerName);
    }

    public Dealer (String dealerName, int cashTakeOut){
        super(0,dealerName);
        dealerCashTakeOut = cashTakeOut;
    }

    public void showLeaderBoardRank(int dealerRank){
        System.out.println((dealerRank + 1) + ". " + super.getPlayerName() + " (" + dealerCashTakeOut + ") ");
    }

    public int getTakeHomeCash() {
        return dealerCashTakeOut;
    }

    public void updateBalance(List <Player> loserList, int potMoney, int totalWinMoney) {
        if (loserList.contains(this)){
            super.balance -= potMoney;
            super.balance += totalWin(loserList,totalWinMoney);
        } else {
            super.balance += totalWinMoney;
        }

    }

    public int totalLoss(List<Player> winnerList, int potMoney) {
        return winnerList.contains(this) ? 0 : potMoney;
    }

    public int totalWin(List<Player> loserList, int totalWinMoney){
        int sumOfLoserBets = loserList.stream().mapToInt(Player :: getBetAmount).sum();
        return loserList.contains(this) ? sumOfLoserBets : totalWinMoney == 0 ? 0 : totalWinMoney;
    }

}
