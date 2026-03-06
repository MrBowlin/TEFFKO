package framework;

import io.calimero.DetachEvent;
import io.calimero.process.ProcessEvent;
import io.calimero.process.ProcessListener;

public class BusListener implements ProcessListener {
    final private EventBuffer responseBuffer = new EventBuffer();

    public void clearBuffer() {
        responseBuffer.clear();
    }

    public EventBuffer getBuffer() {
        return responseBuffer;
    }

	@Override
	public void groupWrite(final ProcessEvent e) { 
        responseBuffer.add(new EventInfo(
        "INCOMING",
        "WRITE.INDICATION",
        e.getSourceAddr().toString(),
        e.getDestination().toString(),
        e.getASDU()
    )); }

	@Override
	public void groupReadRequest(final ProcessEvent e) { 
        responseBuffer.add(new EventInfo(
        "INCOMING",
        "READ.REQUEST",
        e.getSourceAddr().toString(),
        e.getDestination().toString(),
        e.getASDU()
    )); }

	@Override
	public void groupReadResponse(final ProcessEvent e) { 
        responseBuffer.add(new EventInfo(
        "INCOMING",
        "READ.RESPONSE",
        e.getSourceAddr().toString(),
        e.getDestination().toString(),
        e.getASDU()
    )); }

	@Override
	public void detached(final DetachEvent e) {}
}
