package tests;

import io.calimero.DetachEvent;
import io.calimero.GroupAddress;
import io.calimero.process.ProcessEvent;
import io.calimero.process.ProcessListener;

public class BusListener implements ProcessListener {

    private EventInfo event;

    public EventInfo awaitMessage(GroupAddress address, long timeout) throws InterruptedException {
        event = null;
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        while (timeout > currentTime - startTime && (event == null || event.destinationAddress.equals(address.toString()))) {
            Thread.sleep(500);
            currentTime = System.currentTimeMillis();
        }
        if (event != null && event.destinationAddress.equals(address.toString())) {
            return this.event;
        } else {
            return new EventInfo("Timout of " + timeout + " reached, no message received for GroupAddress " + address.toString());
        }
    }       

    @Override
	public void groupWrite(final ProcessEvent e) { 
        event = new EventInfo(
            "INCOMING",
            "WRITE.INDICATION",
            e.getSourceAddr().toString(),
            e.getDestination().toString(),
            e.getASDU()
        ); 
    }

	@Override
	public void groupReadRequest(final ProcessEvent e) { 
        event = new EventInfo(
            "INCOMING",
            "READ.REQUEST",
            e.getSourceAddr().toString(),
            e.getDestination().toString(),
            e.getASDU()
        ); 
    }

	@Override
	public void groupReadResponse(final ProcessEvent e) { 
        event = new EventInfo(
            "INCOMING",
            "READ.RESPONSE",
            e.getSourceAddr().toString(),
            e.getDestination().toString(),
            e.getASDU()
        ); 
    }

    @Override
	public void detached(final DetachEvent e) {}
}
