

public class SimpleHost extends Host {
    private int durTimerId;
    private int intTimerId;
    private int currentSendAddress;
    private int currentInterval;

    SimpleHost() {
        super();
    }

    @Override
    protected void receive(Message msg) {
        // Determine the message type by looking at the value of the string message
        String messageType = msg.getMessage();

        // Handle ping request
        if (messageType.equals("PING_REQUEST")) {
            // Reply with a ping response
            System.out.println("[" + this.getCurrentTime() + "ts] Host " + this.getHostAddress() + ": Ping request from host " + msg.getSrcAddress());
            sendPingResponse(msg.getSrcAddress());
        }
        // Handle ping response
        else if (messageType.equals("PING_RESPONSE")) {
            // Compute RTT (Round-Trip Time)
            int currentTime = getCurrentTime();
            int rtt = msg.getDistance()*2;
            System.out.println("[" + this.getCurrentTime() + "ts] Host " + this.getHostAddress() + ": Ping response from host "
            + msg.getSrcAddress() + " (RTT = " + rtt + "ts)");
        }
    }

    @Override
    protected void timerExpired(int eventId) {
        if (eventId == durTimerId) {
            System.out.println("[" + this.getCurrentTime() + "ts] Host " + this.getHostAddress() + ": Stopped sending pings");
            this.cancelTimer(intTimerId);
        }
        else if (eventId == intTimerId) {
            Message pingRequest = new Message(this.getHostAddress(), this.currentSendAddress, "PING_REQUEST");
            pingRequest.setInsertionTime(this.getCurrentTime());
            System.out.println("[" + this.getCurrentTime() + "ts] Host " + this.getHostAddress() + ": Sent ping to host " + this.currentSendAddress);
            intTimerId = this.newTimer(this.currentInterval);
            this.sendToNeighbor(pingRequest);
        }
    }

    @Override
    protected void timerCancelled(int eventId) {
        if (eventId == this.durTimerId) {
            this.durTimerId = -1;
        }
        else if (eventId == this.intTimerId) {
            this.intTimerId = -1;
        }
    }

    /**
     * Sends ping requests at a specified interval for a specified duration.
     *
     * @param destAddr destination address of host to send ping requests
     * @param interval amount of time to wait between sending ping requests
     * @param duration total amount of time to send ping requests
     */
    public void sendPings(int destAddr, int interval, int duration) {
        this.intTimerId = this.newTimer(interval);
        this.durTimerId = this.newTimer(duration);
        this.currentSendAddress = destAddr;
        this.currentInterval = interval;
    }

    /**
     * Sends a ping response to the specified host.
     *
     * @param destAddr destination address of the host to send ping response
     */
    private void sendPingResponse(int destAddr) {
        Message pingResponse = new Message(getHostAddress(), destAddr, "PING_RESPONSE");
        pingResponse.setInsertionTime(this.getCurrentTime());
        sendToNeighbor(pingResponse);
    }
}
