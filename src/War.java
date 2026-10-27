import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class War {

    // ADD ADDITIONAL CONSTANTS HERE, IF DESIRED...

    static Pile pile1;
    static Pile pile2;
    static int numTurns;

    // ADD ADDITIONAL VARIABLES HERE, IF DESIRED...

    public static void printPiles() {

        // For your convenience, this prints the cards in
        // pile1 and pile2 along with the turn number, as specified
        // by numTurns

        System.out.println("Turn: " + numTurns + System.lineSeparator());
        System.out.println("player1:");
        System.out.println(pile1.toString() + System.lineSeparator());
        System.out.println("player2:");
        System.out.println(pile2.toString());
        System.out.println("--------------------------------------");
    }

    public static void printWar() {

        // Call this method every time there is a tie in the ranks
        // of cards compared

        System.out.println("                                        *** WAR! ***" + System.lineSeparator());
    }

    // ADD ADDITIONAL METHODS HERE, IF DESIRED...
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java War <deck-file>");
            return;
        }

        // args[0] is the deck filename.
        try {
            Scanner scanner = new Scanner(new File(args[0]));
            System.out.println("Loading deck from: " + args[0]);
            pile1 = new Pile(scanner.nextLine());
            pile2 = new Pile(scanner.nextLine());
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found: " + args[0]);
            return;
        }

        numTurns = 1;
        while (numTurns <= 5000 && !pile1.isEmpty() && !pile2.isEmpty()) {
            printPiles();
            Pile pot = new Pile("");
            Card card1 = pile1.dequeue();
            Card card2 = pile2.dequeue();
            pot.enqueue(card1);
            pot.enqueue(card2);
            Pile winner = null;

            while (card1.compareTo(card2) == 0) {
                printWar();
                if (pile1.size() < 4 || pile2.size() < 4) {
                    Pile loser;
                    if (pile1.size() < 4) {
                        winner = pile2;
                        loser = pile1;
                    } else {
                        winner = pile1;
                        loser = pile2;
                    }
                    while (!loser.isEmpty()) {
                        pot.enqueue(loser.dequeue());
                    }
                    break;
                }

                for (int i = 0; i < 4; i++) {
                    card1 = pile1.dequeue();
                    card2 = pile2.dequeue();
                    pot.enqueue(card1);
                    pot.enqueue(card2);
                }
            }

            if (winner == null) {
                if (card1.compareTo(card2) > 0) {
                    winner = pile1;
                } else {
                    winner = pile2;
                }
            }
            while (!pot.isEmpty()) {
                winner.enqueue(pot.dequeue());
            }
            numTurns++;
        }

        if (pile1.isEmpty()) {
            System.out.println("player 2 wins!");
        } else if (pile2.isEmpty()) {
            System.out.println("player 1 wins!");
        } else {
            System.out.println("draw!");
        }
    }
}
