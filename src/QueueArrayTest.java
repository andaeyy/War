public class QueueArrayTest {
    private static void check(String testName, boolean condition) {
        System.out.println((condition ? "PASS: " : "FAIL: ") + testName);
        if (!condition) {
            throw new AssertionError(testName);
        }
    }

    private static boolean hasRemainingInOrder(QueueArray<String> queue, int first, int last) {
        for (int i = first; i <= last; i++) {
            if (queue.isEmpty() || !queue.dequeue().equals("item" + i)) {
                return false;
            }
        }
        return queue.isEmpty();
    }

    public static void main(String[] args) {
        QueueArray<String> growing = new QueueArray<>();
        for (int i = 0; i < 16; i++) {
            growing.enqueue("item" + i);
        }
        check("enqueue growth preserves all 16 items in FIFO order",
                growing.size() == 16 && hasRemainingInOrder(growing, 0, 15));
        check("queue is empty after removing all grown items", growing.isEmpty());

        QueueArray<String> shrinking = new QueueArray<>();
        for (int i = 0; i < 16; i++) {
            shrinking.enqueue("item" + i);
        }
        boolean removedInOrder = true;
        for (int i = 0; i < 12; i++) {
            if (!shrinking.dequeue().equals("item" + i)) {
                removedInOrder = false;
                break;
            }
        }
        boolean remainingInOrder = true;
        for (int i = 12; i < 16; i++) {
            if (shrinking.isEmpty() || !shrinking.dequeue().equals("item" + i)) {
                remainingInOrder = false;
                break;
            }
        }
        check("dequeue shrink preserves order and remaining items",
                removedInOrder && remainingInOrder && shrinking.isEmpty());

        QueueArray<String> wrapped = new QueueArray<>();
        for (int i = 0; i < 6; i++) {
            wrapped.enqueue("item" + i);
        }
        boolean firstRemovalsCorrect = wrapped.dequeue().equals("item0")
                && wrapped.dequeue().equals("item1")
                && wrapped.dequeue().equals("item2");
        wrapped.enqueue("item6");
        wrapped.enqueue("item7");
        wrapped.enqueue("item8");
        boolean wraparoundOrder = firstRemovalsCorrect
                && wrapped.size() == 6
                && wrapped.dequeue().equals("item3")
                && wrapped.dequeue().equals("item4")
                && wrapped.dequeue().equals("item5")
                && wrapped.dequeue().equals("item6")
                && wrapped.dequeue().equals("item7")
                && wrapped.dequeue().equals("item8")
                && wrapped.isEmpty();
        check("wraparound keeps FIFO order after enqueue and dequeue", wraparoundOrder);

        QueueArray<String> empty = new QueueArray<>();
        boolean emptyDequeueThrows = false;
        try {
            empty.dequeue();
        } catch (RuntimeException expected) {
            emptyDequeueThrows = true;
        }
        check("dequeue on empty queue throws RuntimeException", emptyDequeueThrows);
    }
}
