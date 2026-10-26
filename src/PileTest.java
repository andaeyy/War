import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PileTest {
    private static void check(String testName, boolean condition) {
        System.out.println((condition ? "PASS: " : "FAIL: ") + testName);
    }

    private static List<String> cards(Pile pile) {
        List<String> result = new ArrayList<>();
        for (Card card : pile) {
            result.add(card.toString());
        }
        return result;
    }

    public static void main(String[] args) {
        Pile pile = new Pile("2c 3c 4c 5c 6c");
        check("constructor preserves card order",
                cards(pile).equals(Arrays.asList("2c", "3c", "4c", "5c", "6c")));
        check("size after construction", pile.size() == 5);

        pile.enqueue(new Card('7', 'c'));
        check("enqueue increases size and adds at back",
                pile.size() == 6 && cards(pile).equals(
                        Arrays.asList("2c", "3c", "4c", "5c", "6c", "7c")));

        Card removed = pile.dequeue();
        check("dequeue returns front card and decreases size",
                removed.toString().equals("2c") && pile.size() == 5
                        && cards(pile).equals(Arrays.asList("3c", "4c", "5c", "6c", "7c")));

        Pile empty = new Pile("");
        check("isEmpty is true for empty pile", empty.isEmpty());
        check("isEmpty is false for non-empty pile", !pile.isEmpty());
        check("size reflects dequeue", pile.size() == 5);

        Pile shortPile = new Pile("2c 3c 4c");
        check("toString formats fewer than 10 cards without an internal newline",
                shortPile.toString().equals("2c  3c  4c  "));

        Pile longPile = new Pile("2c 3c 4c 5c 6c 7c 8c 9c Tc Jc Qc");
        String expectedFormatting = "2c  3c  4c  5c  6c  7c  8c  9c  Tc  Jc  "
                + "\n" + "Qc  ";
        check("toString starts a new line after the 10th card",
                longPile.toString().equals(expectedFormatting));

        Pile iteratorPile = new Pile("4h 5d 6s 7c");
        Iterator<Card> iterator = iteratorPile.iterator();
        boolean iteratorOrder = true;
        String[] iteratorExpected = {"4h", "5d", "6s", "7c"};
        for (String expected : iteratorExpected) {
            if (!iterator.hasNext() || !iterator.next().toString().equals(expected)) {
                iteratorOrder = false;
                break;
            }
        }
        iteratorOrder = iteratorOrder && !iterator.hasNext();
        check("iterator returns cards front-to-back", iteratorOrder);

        Pile shufflePile = new Pile("2c 3c 4c 5c 6c 7c");
        shufflePile.shuffle();
        check("shuffle preserves card count", shufflePile.size() == 6);
        check("riffle shuffle interleaves the two halves exactly",
                cards(shufflePile).equals(Arrays.asList("2c", "5c", "3c", "6c", "4c", "7c")));
    }
}
