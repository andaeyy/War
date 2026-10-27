import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WarTest {
    private static void check(String testName, boolean condition) {
        System.out.println((condition ? "PASS: " : "FAIL: ") + testName);
        if (!condition) {
            throw new AssertionError(testName);
        }
    }

    private static String runGame(String filename) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(captured));
            War.main(new String[]{filename});
        } finally {
            System.setOut(originalOut);
        }
        return captured.toString();
    }

    private static String runDeck(String cards1, String cards2) throws IOException {
        Path deck = Files.createTempFile("war-test-", ".txt");
        try {
            Files.writeString(deck, cards1 + "\n" + cards2 + "\n");
            return runGame(deck.toString());
        } finally {
            Files.deleteIfExists(deck);
        }
    }

    private static String cards(Pile pile) {
        return pile.toString().trim().replaceAll("\\s+", " ");
    }

    private static int count(String output, String text) {
        return output.split(text, -1).length - 1;
    }

    private static boolean hasTerminalResult(String output) {
        return output.contains("player 1 wins!")
                || output.contains("player 2 wins!")
                || output.contains("draw!");
    }

    public static void main(String[] args) throws IOException {
        String deck1Output = runGame("lib/deck1.txt");
        check("deck1: sample-run game ends with player 2 winning",
                deck1Output.contains("player 2 wins!"));

        String deck2Output = runGame("lib/deck2.txt");
        check("deck2: simultaneous shortages apply player 1's war loss first",
                deck2Output.contains("player 2 wins!")
                        && War.pile1.isEmpty() && War.pile2.size() == 52);

        String deck3Output = runGame("lib/deck3.txt");
        check("deck3: even-vs-odd game ends with player 2 winning",
                deck3Output.contains("player 2 wins!"));

        String deck4Output = runGame("lib/deck4.txt");
        check("deck4: mixed deck reaches a valid terminal result",
                hasTerminalResult(deck4Output));

        String player1Win = runDeck("As", "2c");
        check("normal player 1 win collects player 1's card first",
                player1Win.contains("player 1 wins!")
                        && cards(War.pile1).equals("As 2c") && War.pile2.isEmpty());

        String player2Win = runDeck("2c", "As");
        check("normal player 2 win still collects player 1's card first",
                player2Win.contains("player 2 wins!")
                        && cards(War.pile2).equals("2c As") && War.pile1.isEmpty());

        String warWin = runDeck("Jc 2h 8c 7h Ah", "Jd 5s 9d 4c 3s");
        check("war keeps all ten cards in the required order",
                warWin.contains("player 1 wins!") && count(warWin, "WAR!") == 1
                        && cards(War.pile1).equals("Jc Jd 2h 5s 8c 9d 7h 4c Ah 3s")
                        && War.pile2.isEmpty());

        String repeatedWar = runDeck("Jc 2h 8c 7h Qc 2c 3c 4h Ah",
                "Jd 5s 9d 4c Qd 5h 6h 7s 3s");
        check("repeated wars print every tie and keep the entire pot in order",
                repeatedWar.contains("player 1 wins!") && count(repeatedWar, "WAR!") == 2
                        && cards(War.pile1).equals(
                                "Jc Jd 2h 5s 8c 9d 7h 4c Qc Qd 2c 5h 3c 6h 4h 7s Ah 3s")
                        && War.pile2.isEmpty());

        String player1Short = runDeck("Jc 2h 3h 4h", "Jd 5s 9d 4c 3s");
        check("player 1 loses when unable to complete a war",
                player1Short.contains("player 2 wins!") && War.pile1.isEmpty()
                        && War.pile2.size() == 9);

        String player2Short = runDeck("Jc 5s 9d 4c 3s", "Jd 2h 3h 4h");
        check("player 2 loses when unable to complete a war",
                player2Short.contains("player 1 wins!") && War.pile2.isEmpty()
                        && War.pile1.size() == 9);

        String continuingShortage = runDeck("Jc 2h 8c 7h Qc 2c",
                "Jd 5s 9d 4c Qd 5h 6h 7s 3s");
        check("a shortage during a continuing war is a loss",
                continuingShortage.contains("player 2 wins!")
                        && count(continuingShortage, "WAR!") == 2
                        && War.pile1.isEmpty() && War.pile2.size() == 15);

        String draw = runDeck("2c 3c 5c", "6d 7d 4d");
        check("a cycling game stops after exactly 5000 turns with a draw",
                draw.endsWith("draw!" + System.lineSeparator())
                        && count(draw, "Turn: ") == 5000 && !draw.contains("Turn: 5001")
                        && War.pile1.size() + War.pile2.size() == 6
                        && !War.pile1.isEmpty() && !War.pile2.isEmpty());
    }
}
