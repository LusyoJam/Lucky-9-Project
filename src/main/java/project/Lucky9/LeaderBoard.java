package project.Lucky9;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderBoard {
    private final List <Dealer> leaderBoardRanking;

    public LeaderBoard(){
        leaderBoardRanking = new ArrayList<>();
        initializeLeaderBord();
    }

    private void initializeLeaderBord(){
        leaderBoardRanking.add(new Dealer("LayBaiMySyde", 9999999));
        leaderBoardRanking.add(new Dealer("KyNessAshChan", 123456789));
        leaderBoardRanking.add(new Dealer("Irishhh <3", 143333333));
    }

    public void addToLeaderBoard(Dealer dealer){
        leaderBoardRanking.add(dealer);
    }

    public void showLeaderBoard(){
        leaderBoardRanking.sort(Comparator.comparing(Dealer::getTakeHomeCash).reversed());
        System.out.println("----------------------LEADERBOARD------------------------");
        for (int dealerCounter = 0; dealerCounter < leaderBoardRanking.size(); dealerCounter++){
            leaderBoardRanking.get(dealerCounter).showLeaderBoardRank(dealerCounter);
        }
        System.out.println("---------------------------------------------------------");

    }
}
