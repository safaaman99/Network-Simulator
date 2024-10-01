public class Node {
    Event event;
    Node next;
    Node prev;

    Node(Event event) {
        this.event = event;
        this.next = null;
        this.prev = null;
    }
}
