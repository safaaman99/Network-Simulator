/**
 * Message event.
 * Represents a network message.
 */
public class Message extends Event {

    private final int srcAddress;
    private final int destAddress;
    private final String message;
    private Host nextHop;
    private int distance;

    public Message(int srcAddress, int destAddress, String message) {
        super();
        this.srcAddress = srcAddress;
        this.destAddress = destAddress;
        this.message = message;
    }

    /**
     * Returns the source address of this Message.
     *
     * @return the source address
     */
    public int getSrcAddress() {
        return srcAddress;
    }

    /**
     * Returns the destination address of this Message.
     *
     * @return the destination address
     */
    public int getDestAddress() {
        return destAddress;
    }

    /**
     * Returns the string message of this Message.
     *
     * @return the string message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the next hop host and distance for this Message.
     * Used by the sendToNeighbor method in the Host class.
     *
     * @param nextHop the next hop host
     * @param distance the distance to the next hop
     */
    public void setNextHop(Host nextHop, int distance) {
        this.nextHop = nextHop;
        this.distance = distance;
        this.arrivalTime = this.insertionTime + distance;
    }

    /**
     * Returns the next hop host for this Message.
     *
     * @return the next hop host
     */
    public Host getNextHop() {
        return nextHop;
    }

    /**
     * Returns the distance to the next hop for this Message.
     *
     * @return the distance to the next hop
     */
    public int getDistance() {
        return distance;
    }

    @Override
    public void setInsertionTime(int currentTime) {
        this.insertionTime = currentTime;
    }

    @Override
    public void cancel() {
        // No action needed for message cancellation
    }

    @Override
    public void handle() {
        this.nextHop.receive(this);
    }
}