public class LinkedEventList implements FutureEventList {
    private Node head;
    private int size;

    private int simulationtime;

    LinkedEventList() {
        this.size = 0;
        this.simulationtime = 0;
        this.head = null;
    }

    @Override
    public Event removeFirst() {
        if (head == null) {
            return null; // List is empty
        }
        Event removedEvent = head.event;
        head = head.next;
        if (head != null) {
            head.prev = null;
        }
        size--;
        simulationtime = removedEvent.getArrivalTime();

        return removedEvent;
    }

    @Override
    public boolean remove(Event e) {
        Node current = head;
        while (current != null) {
            if (current.event.equals(e)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                }
                size--;
                return true; // Event found and removed
            }
            current = current.next;
        }
        return false; // Event not found
    }

    @Override
    public void insert(Event e) {
        Node newNode = new Node(e);
        if (newNode.event instanceof Timer) {
            newNode.event.setInsertionTime(simulationtime);
        }

        if (head == null) {
            head = newNode;
        }
        else if(head.event.getArrivalTime() > e.getArrivalTime()) {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        }
        else {
            Node current = head;
            while (current.next != null) {
                if (current.next.event.getArrivalTime() > e.getArrivalTime()) {
                    break;
                } else {
                    current = current.next;
                }
            }
            newNode.prev = current;
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int capacity() {
        return size; // Capacity is the same as the size in a linked list
    }

    @Override
    public int getSimulationTime() {
        return this.simulationtime;
    }
}