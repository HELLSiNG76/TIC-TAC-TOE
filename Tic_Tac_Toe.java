import java.util.*;

public class Tic_Tac_Toe {

    // Enum for representing the two possible players
    enum Player {
        X, O;

        // Swap X to O and vice versa
        Player switchPlayer() {
            return this == X ? O : X;
        }
    }

    // Board class tracks the state and handles most board logic
    static class Board {
        private final String[] squares = new String[9];

        // All possible ways to win
        private static final int[][] LINES = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        Board() {
            for (int i = 0; i < 9; i++) {
                squares[i] = String.valueOf(i + 1);
            }
        }

        boolean isMoveValid(int pos) {
            return pos >= 1 && pos <= 9 && squares[pos - 1].matches("[1-9]");
        }

        void placeMove(int pos, Player who) {
            squares[pos - 1] = who.name();
        }

        boolean isBoardFull() {
            for (String square : squares) {
                if (square.matches("[1-9]")) return false;
            }
            return true;
        }

        Player getWinner() {
            for (int[] line : LINES) {
                String s1 = squares[line[0]];
                String s2 = squares[line[1]];
                String s3 = squares[line[2]];
                if (s1.equals(s2) && s2.equals(s3) && s1.matches("[XO]")) {
                    return Player.valueOf(s1);
                }
            }
            return null;
        }

        void showBoard() {
            System.out.println("\n  Current board:");
            System.out.println("  =============");
            for (int i = 0; i < 9; i += 3) {
                System.out.printf("   %s | %s | %s\n", squares[i], squares[i+1], squares[i+2]);
                if (i < 6) System.out.println("  ---+---+---");
            }
            System.out.println("  =============\n");
        }

        void showReferenceBoard() {
            System.out.println("Position guide:");
            System.out.println("  1 | 2 | 3");
            System.out.println(" ---+---+---");
            System.out.println("  4 | 5 | 6");
            System.out.println(" ---+---+---");
            System.out.println("  7 | 8 | 9\n");
        }
    }

    static class Game {
        private final Scanner in = new Scanner(System.in);
        private final Board board = new Board();
        private Player currentPlayer = Player.X;
        private boolean playWithAI = false;
        private final Random rng = new Random();
        private String xName = "X";
        private String oName = "O";

        void run() {
            System.out.println("Hello! Welcome to Tic-Tac-Toe.");
            setUpPlayers();
            board.showReferenceBoard();
            board.showBoard();

            while (true) {
                if (playWithAI && currentPlayer == Player.O) {
                    aiTurn();
                } else {
                    humanTurn();
                }
                board.showBoard();

                Player winner = board.getWinner();
                if (winner != null) {
                    System.out.println("🎉 Congrats, " + getName(winner) + " has won the game!");
                    break;
                }
                if (board.isBoardFull()) {
                    System.out.println("It's a draw! Well played both.");
                    break;
                }
                currentPlayer = currentPlayer.switchPlayer();
            }
            playAgainPrompt();
        }

        void setUpPlayers() {
            System.out.print("Enter a name for X: ");
            String input = in.nextLine().trim();
            if (!input.isEmpty()) xName = input;

            System.out.print("Play with a friend (1) or AI (2)? Enter 1 or 2: ");
            int mode = 0;
            while (mode != 1 && mode != 2) {
                try {
                    mode = in.nextInt();
                } catch (InputMismatchException e) {
                    in.nextLine();
                }
                if (mode != 1 && mode != 2) {
                    System.out.print("Please type 1 for human or 2 for AI: ");
                }
            }
            in.nextLine(); // clear input buffer
            playWithAI = (mode == 2);
            if (!playWithAI) {
                System.out.print("Enter a name for O: ");
                String oInput = in.nextLine().trim();
                if (!oInput.isEmpty()) oName = oInput;
            } else {
                oName = "Computer";
            }
            System.out.println("Game on! " + xName + " is X, " + oName + " is O.");
        }

        String getName(Player p) {
            return p == Player.X ? xName : oName;
        }

        void humanTurn() {
            int move = -1;
            while (true) {
                System.out.print(getName(currentPlayer) + " (" + currentPlayer + "), pick a square (1-9): ");
                try {
                    move = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.print("Not a number. Try again. ");
                    in.nextLine(); // clear
                    continue;
                }
                if (board.isMoveValid(move)) {
                    board.placeMove(move, currentPlayer);
                    in.nextLine(); // move past newline
                    break;
                } else {
                    System.out.println("That spot isn't available. Choose another!");
                }
            }
        }

        void aiTurn() {
            System.out.println("Computer is thinking...");
            // Pause for effect
            try { Thread.sleep(700); } catch (InterruptedException ignored) {}
            // Simple AI: random available slot
            List<Integer> open = new ArrayList<>();
            for (int i = 1; i <= 9; i++) {
                if (board.isMoveValid(i)) open.add(i);
            }
            int aiMove = open.get(rng.nextInt(open.size()));
            System.out.println("Computer chooses " + aiMove + ".");
            board.placeMove(aiMove, currentPlayer);
        }

        void playAgainPrompt() {
            System.out.print("Would you like to play again? (y/n): ");
            String answer = in.next().trim().toLowerCase();
            in.nextLine();
            if (answer.equals("y")) {
                // Reset board, reset player to X, begin again
                for (int i = 0; i < 9; i++) {
                    board.squares[i] = String.valueOf(i + 1);
                }
                currentPlayer = Player.X;
                board.showReferenceBoard();
                board.showBoard();
                run();
            } else {
                System.out.println("Thanks for playing. Until next time!");
            }
        }
    }

    public static void main(String[] args) {
        new Game().run();
    }
}
