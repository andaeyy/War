import java.util.Scanner;
public class Pile extends QueueArray<Card> {
    public Pile(String cards) {
// Allows for the construction of a pile of cards from a string
//
// Example:
// Pile pile = new Pile("As 2s 3s Jh Qh Kh 4d 5d 6d 7c 8c 9c");
        Scanner scanner = new Scanner(cards);
        while (scanner.hasNext()) {
            String card = scanner.next();
            this.enqueue(new Card(card.charAt(0),card.charAt(1)));
        }
        scanner.close();
    }
    public String toString() {
// Creates a string containing 10 cards per line, for as many

// as necessary to show all cards in the pile
//
// Example:
// 6h 3h Jc Ts 3c 7h 2h 9c 3s 7s
// 8c 5d Js Jh 6d Ah Qh 9d Ks 6c
// Kc Td Jc Th 9s Kd
        String s = "";
        int i = 0;
        for (Card card : this) {
            s += card + "  ";
            if (i % 10 == 9)
                s += "\n";
            i = (i+1) % 10;
        }
        return s;
    }
    /*
    * A riffle shuffle cuts the pile into two halves and then
    interleaves
    * them by alternating cards from each half. Split this pile so the
    first
    * half contains the first size/2 cards and the second half contains
    all
    * remaining cards. If the pile has an odd number of cards, the first
    half
    * gets the extra card.
    *
    * Then repeatedly dequeue one card from the first half and enqueue
    it
    * back into this pile, followed by dequeuing one card from the
    second half
    * and enqueuing it back into this pile. Continue alternating until
    both
    * halves are empty; handle the final card from a half if the halves
    have
    * different sizes. Use size() to determine the cut and isEmpty() to
    check
    * whether a half has cards; dequeue() removes a card and enqueue()
    adds it.
    *
    * Hint: Use two temporary Pile objects (or QueueArray<Card> objects)
    to
    * hold the two halves while you rebuild this pile.
    */
    public void shuffle() {
        int halfsize;
        if (this.size() % 2 == 1) halfsize = (this.size()+1)/2;
        else halfsize = this.size()/2;
        QueueArray<Card> firsthalf = new QueueArray<Card>();
        QueueArray<Card> secondhalf = new QueueArray<Card>();
        int count = 0;
        for (Card c : this){
            if (count < halfsize) firsthalf.enqueue(this.dequeue());
            else secondhalf.enqueue(this.dequeue());
            count++;
        }
        while (!secondhalf.isEmpty()){
            this.enqueue(firsthalf.dequeue());
            this.enqueue(secondhalf.dequeue());
        }
        while (!firsthalf.isEmpty()){
            this.enqueue(firsthalf.dequeue());
        }
    }
}


