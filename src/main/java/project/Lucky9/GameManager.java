package project.Lucky9;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GameManager {
    private final LeaderBoard leaderBoard = new LeaderBoard();
    private final Scanner userInput;
    private List<Player> playerList;
    private List<Player> winnerList;
    private final List<Player> loserList;
    private Dealer dealer;
    private final Random randomizerForBet;
    private int totalPotMoney;
    private final int MINIMUM_BET = 20;
    private final int MAXIMUM_BET = 101; // 101 because nextInt() is not closed-bounded

    public GameManager() {
        userInput = new Scanner(System.in);
        playerList = new ArrayList<>();
        randomizerForBet = new Random();
        winnerList = new ArrayList<>();
        loserList = new ArrayList<>();
    }

    // Introductory Phase
    public void mainMenu() {
        System.out.println("""
                =============================================================================
                ----------------------------[WELCOME TO LUCKY 9]-----------------------------
                =============================================================================
                """);

        System.out.print(
                "1) Start Game\n" +
                        "2) View Leaderboard\n" +
                        "3) Exit Game\n" +
                        "Choose from the choices above to proceed: "
        );

        try {
            byte userChoice = userInput.nextByte();
            userInput.nextLine();

            switch (userChoice) {
                case 1 -> startGame();
                case 2 -> viewLeaderboard();
                case 3 -> exitGame();
                default -> System.out.println("Invalid choice. Please select from the above choices only.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please only enter numbers within 1-3.");
            userInput.nextLine();
        }
    }

    private void startGame() {
        try {
            System.out.print("Please enter your dealer name: ");
            String dealerName = userInput.nextLine();
            if (dealerName.isEmpty()) throw new InvalidDealerNameException();

            System.out.print("Please enter your cash fund: ");
            int dealerFund = userInput.nextInt();
            if (dealerFund <= 0) throw new InvalidDealerFundException();

            System.out.print("Please enter number of player(s): ");
            byte numberOfPlayers = userInput.nextByte();
            System.out.println();
            if (numberOfPlayers <= 0 || numberOfPlayers > 20) throw new InvalidNumberOfPlayerException();

            // Initializes the game
            initializeGame(dealerName, dealerFund, numberOfPlayers);

        } catch (InvalidDealerNameException e) {
            System.out.println("Dealer name can't be empty or the same as default names (e.g., Player 1...20).\n");
        } catch (InvalidDealerFundException e) {
            System.out.println("Initial fund can't be empty or negative.\n");
        } catch (InvalidNumberOfPlayerException e) {
            System.out.println("Number of players must be between 1 and 20.\n");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.\n");
            userInput.nextLine();
        }
    }

    private void initializeGame(String dealerName, int initialFund, int playerNumber) {
        dealer = new Dealer(initialFund, dealerName);
        leaderBoard.addToLeaderBoard(dealer);

        for (int playerCounter = 0; playerCounter < playerNumber; playerCounter++) {
            Player player = new Player(initialFund, "Player " + (playerCounter + 1));
            playerList.add(player);
        }

        // Set the bets for each player
        setBets();
        // Next phase
        playGame();
    }

    public void setBets() {
        playerList.forEach(player -> player.showBet(randomizerForBet.nextInt(MINIMUM_BET, MAXIMUM_BET)));
        totalPotMoney = totalBets();
    }

    public int totalBets() {
        return playerList.stream()
                .mapToInt(Player::getBetAmount)
                .sum();
    }

    private void playGame() {
        System.out.print("\n1. Play\n2. Quit\nPlease enter your choice: ");

        try {
            byte userChoice = userInput.nextByte();
            userInput.nextLine();

            switch (userChoice) {
                case 1 -> executeGameRound();
                case 2 -> {
                    System.out.println("Exiting..");
                    mainMenu();
                    dealer.getTakeHomeCash();
                    leaderBoard.addToLeaderBoard(dealer);
                }
                default -> System.out.println("Invalid choice. Please choose from the choices above only.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            userInput.nextLine();
        }
    }

    private void executeGameRound() {
        Deck deck = new Deck();

        dealer.addCard(deck.distributeCards());
        dealer.addCard(deck.distributeCards());

        for (Player player : playerList) {
            for (int playerHandNumber = 0; playerHandNumber < 2; playerHandNumber++) {
                if (playerHandNumber == 1 && PlayerController.canTakeMoreCard(player)) {
                    player.addCard(deck.distributeCards());
                    player.addCard(deck.distributeCards());
                } else {
                    player.addCard(deck.distributeCards());
                }
            }
        }

        System.out.println(dealer);
        if (!(PlayerController.isDeckThreeCards(dealer))) {
            System.out.print("Draw another card (Y/N): ");
            char drawChoice = userInput.nextLine().toUpperCase().charAt(0);
            if (drawChoice == 'Y') {
                dealer.addCard(deck.distributeCards());
            } else if (drawChoice != 'N') {
                System.out.println("Invalid choice. Please enter Y or N.");
            }
        }

        System.out.print(dealer + "Hit ENTER to view other player's cards..");
        userInput.nextLine();
        System.out.println("\n===================================================");
        playerList.forEach(System.out::println);
        System.out.println(dealer);
        standOffRound();
    }

    public void standOffRound() {
        setListOfWinnersAndLosers();

        if (winnerList.size() > 1) {
            winnerList.forEach(player -> player.addWinnings(totalWinMoney()));
        } else {
            winnerList.get(0).addWinnings(totalPotMoney);
        }

        loserList.forEach(Player::deductFromBalance);

        resultOfRound();
        dealerResult();

        winnerList.clear();
        loserList.clear();
        playerList.forEach(Player :: emptyPlayerHand);
        dealer.emptyPlayerHand();
        playAgain();
    }

    public void setListOfWinnersAndLosers() {

        Predicate <Player> isLucky9 =
                player -> player.getTotalCardsValue() == 9 && !PlayerController.isDeckThreeCards(player);

        Player winningPlayer = playerList.stream()
                .max(Comparator.comparingInt(Player::getTotalCardsValue))
                .get();

        if (dealer.getTotalCardsValue() >= winningPlayer.getTotalCardsValue()) {
            winnerList.add(dealer);
        } else {
            loserList.add(dealer);
        }

        Map<Boolean, List<Player>> winnerOrLoserList = playerList.stream()
                .collect(Collectors.partitioningBy(player -> player.getTotalCardsValue() >= winningPlayer.getTotalCardsValue()));

        winnerList.addAll(winnerOrLoserList.get(true));
        loserList.addAll(winnerOrLoserList.get(false));

        Map<Boolean, List<Player>> areLucky9 = winnerList.stream()
                .collect(Collectors.partitioningBy(isLucky9));

        if (!areLucky9.get(true).isEmpty()) {
            winnerList = areLucky9.get(true);
            loserList.addAll(areLucky9.get(false));
        }

        Player highestValuePlayer =
                winnerList.stream().max((player1, player2) -> player1.getTotalCardsValue() - player2.getTotalCardsValue()).get();

        winnerList =
                winnerList.stream().filter(player -> highestValuePlayer.getTotalCardsValue() == player.getTotalCardsValue()).collect(Collectors.toList());
    }

    public void resultOfRound() {
        String listOfWinners = "";
        String listOfLosers = "";

        for (Player winner : winnerList) {
            listOfWinners += (winner.getPlayerName() + "\n");
        }
        for (Player loser : loserList) {
            listOfLosers += (loser.getPlayerName() + "\n");
        }
        System.out.println("---POT MONEY: " + totalPotMoney + "---");
        System.out.println("\n------WINNER(S)------\n" + listOfWinners + "\nTotal cash winnings " +
                "for winners: " + totalWinMoney() + "\n------LOSER(S)" +
                "-------\n" + listOfLosers);

        //show players' balance
        System.out.println("=====PLAYERS' BALANCE=====\n");
        for(Player players : playerList){
            players.showBalance();
        }
        System.out.println("==========================");
    }

    public void dealerResult() {
        dealer.updateBalance(loserList, totalPotMoney, totalWinMoney());
        System.out.println("----Dealer Result----\nWin: " + dealer.totalWin(loserList, totalPotMoney)+
                "\nLoss:" +
                " " + dealer.totalLoss(winnerList, totalPotMoney) + "\nDealer's total balance: " + dealer.getBalance());

    }

    public void playAgain() {
        removePlayer();
        System.out.print("\nPlay Again? (Y/N): ");
        char dealerDecision = userInput.nextLine().toUpperCase().charAt(0);
        try {
            switch (dealerDecision) {
                case 'Y' -> {
                    setBets();
                    playGame();
                }
                case 'N' -> {
                    System.out.println("Thanks for playing!");
                    mainMenu();
                    dealer.getTakeHomeCash();
                    leaderBoard.addToLeaderBoard(dealer);
                }
                default -> System.out.println("Pick between Y and N only.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Please type valid input (Y or N) only.");
        }
    }

    public void removePlayer() {
        playerList = playerList
                .stream()
                .filter(PlayerController::isBalanceEnough)
                .collect(Collectors.toList());
    }

    public int totalWinMoney(){
        return winnerList.size() > 1 ? 0 : totalPotMoney;
    }

    private void viewLeaderboard() {
        dealer.getTakeHomeCash();
        leaderBoard.showLeaderBoard();
        System.out.println("Press 'Enter' to go back: ");
        userInput.nextLine();
        mainMenu();
    }

    public void notEnoughBalance(){
        if (dealer.getBalance() < MINIMUM_BET){
            System.out.println("Sorry, you can't play anymore. You do not have enough money.");
            dealer.showBalance();
            dealer.getTakeHomeCash();
            leaderBoard.addToLeaderBoard(dealer);
            mainMenu();
        }
    }

    private void exitGame() {
        System.out.println("Thank you for playing Lucky 9!");
        System.exit(0);
    }

    // Custom-made exceptions
    private static class InvalidDealerNameException extends Exception {}
    private static class InvalidDealerFundException extends Exception {}
    private static class InvalidNumberOfPlayerException extends Exception {}
}
