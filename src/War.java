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
    public static void handlewar( Pile pile1, Pile pile2, Pile pile3){
        Card t1 = pile1.dequeue();
        Card t2 = pile2.dequeue();
        pile3.enqueue(t1); pile3.enqueue(t2);
        int counter = 0;
        while (true && counter < 10000) {
            if (t1.Rank().compareTo(t2.Rank) > 0) {
                pile3.enqueue(pile1.dequeue());
                pile3.enqueue(pile2.dequeue());
                pile3.enqueue(pile1.dequeue());
                pile3.enqueue(pile2.dequeue());
            }
            else{
                break;
            }
            t1 = pile1.dequeue();
            t2 = pile2.dequeue();
            counter++;
        }


    }
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
        int TurnTolerance = 1000;
        boolean isWar = false;
        while (TurnTolerance > 0 && !pile1.isEmpty() && !pile2.isEmpty()){
            Pile pile3 = new Pile("");
            Card t1 = pile1.dequeue();
            Card t2 = pile2.dequeue();
            handlewar(pile1, pile2, pile3);

        }
    }
}
