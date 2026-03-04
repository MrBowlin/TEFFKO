package framework;

import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalTime;

import io.calimero.DetachEvent;
import io.calimero.KNXException;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;
import io.calimero.process.ProcessEvent;
import io.calimero.process.ProcessListener;

public class BusListener implements ProcessListener {
	private final String remoteHost;
    private ResponseObject response;
    private String destination;

    public BusListener(String remoteHost) {
        this.remoteHost = remoteHost;
    }

	public ResponseObject awaitMessage(String destinationAddress, long timeout) {
		final var anyLocal = new InetSocketAddress(0);
		final var remote = new InetSocketAddress(remoteHost, 3671);
        final long startTime = System.currentTimeMillis();
        destination = destinationAddress;
        response = null;
		try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, new TPSettings());
		    ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
			pc.addProcessListener(this);
			while (knxLink.isOpen() && System.currentTimeMillis() - startTime < timeout && response == null) {
                Thread.sleep(1000);
            }
            if (response != null) {
                return response;
            }
		}
		catch (final KNXException | InterruptedException | RuntimeException e) {
			return new ResponseObject(e.getMessage());
		}
        return new ResponseObject("Timeout reached, no response from " + destinationAddress + " could be collected.");
	}

	@Override
	public void groupWrite(final ProcessEvent e) { saveResponse("WRITE", e); }
	@Override
	public void groupReadRequest(final ProcessEvent e) { saveResponse("READ_REQUEST", e); }
	@Override
	public void groupReadResponse(final ProcessEvent e) { saveResponse("READ_RESPONSE", e); }
	@Override
	public void detached(final DetachEvent e) {}

	private void saveResponse(final String type, final ProcessEvent e) {
		try {
            if (e.getDestination().toString().equals(destination)) {
                response = new ResponseObject(
                    e.getASDU(),
                    e.getDestination().toString(),
                    e.getSourceAddr().toString(),
                    type,
                    LocalTime.now(),
                    LocalDate.now()
                );
            }
		}
		catch (final RuntimeException ex) {
            response = new ResponseObject(ex.getMessage());
		}
	}

}
