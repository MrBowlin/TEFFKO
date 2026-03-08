package framework;

import java.net.InetSocketAddress;

import io.calimero.KNXException;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;

public class IpBusConnector implements BusConnector {

    private IpBusMonitor monitor;
    private KNXNetworkLink knxLink;
    private ProcessCommunicator pc;
    private boolean monitorActive;

    final private InetSocketAddress anyLocal;
    final private InetSocketAddress remote; 
    final private TPSettings tpSettings;
    private String tunnelingAddress;

    public IpBusConnector(String remoteHost) {
        this.anyLocal = new InetSocketAddress(0);
        this.remote = new InetSocketAddress(remoteHost, 3671);
        this.tpSettings = new TPSettings();
        monitorActive = false;
    }

    @Override
    public void openConnection() {
        try {
            this.knxLink = KNXNetworkLinkIP.newTunnelingLink(this.anyLocal, this.remote, false, this.tpSettings);
            this.tunnelingAddress = this.knxLink.getKNXMedium().assignedAddress().get().toString();
            this.pc = new ProcessCommunicatorImpl(this.knxLink);
            this.monitor = new IpBusMonitor();
            monitorActive = true;
        } catch (KNXException | InterruptedException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        try {
            this.pc.close();
            this.knxLink.close();
            monitorActive = false;
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.err.println("Error closing KNX datapoint: " + e.getMessage());
        }
    }

    @Override
    public EventBuffer getMonitorBuffer() {
        if (this.monitor != null) {
            return monitor.getBuffer();
        } else {
            System.err.println("Could not retrieve Buffer, BusMonitor is null.");
            return new EventBuffer();
        }
    }

    @Override
    public void startMonitor() {
        if (this.pc != null && this.monitor != null) {
            try {
                this.pc.addProcessListener(this.monitor);
                this.monitor.clearBuffer();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                 System.err.println("Error starting KNX Listener: " + e.getMessage());
            }
        }
        else {
            System.err.println("Could not start BusMonitor. Either ProcessCommunicator or BusMonitor is null.");
        }
    }

    @Override
    public void stopMonitor() {
        if (this.pc != null && this.monitor != null) {
            this.pc.removeProcessListener(this.monitor);
        }
        else {
            System.err.println("Could not stop BusListener. Either ProcessCommunicator or BusListener is null.");
        }
    }

    @Override
    public EventInfo awaitMessage(ComObject communicationObject, long timeout) {
        if (this.pc == null) {
            return new EventInfo("Error while waiting for message: BusConnector not initialized correctly, ProcessCommunicator was null!");
        }
        IpBusListener listener = new IpBusListener();
        this.pc.addProcessListener(listener);
        try {
            EventInfo event = listener.awaitMessage(communicationObject.getAddress(), timeout);
            return event;
        } catch(InterruptedException e) {
            return new EventInfo("Error while waiting for message: " + e.getMessage());
        } finally {
            this.pc.removeProcessListener(listener);
        } 
    }

    @Override
    public EventInfo writeMessage(ComObject communicationObject, String value) {
        if (this.pc == null) {
            return new EventInfo("Error while trying to write onto bus: BusConnector not initialized correctly, ProcessCommunicator was null!");
        }
        try {
            EventInfo event = new EventInfo(
                "OUTGOING", 
                "WRITE.INDICATION", 
                this.tunnelingAddress, 
                communicationObject.getAddress().toString(), 
                ComObject.stringToBytes(value, communicationObject.getDPT())
            );
            this.pc.write(communicationObject.getDatapoint(), value);
            if (event.isValid && monitorActive && monitor != null) {
                monitor.addToBuffer(event);
            }
            return event;
        } catch(KNXException e) {
            return new EventInfo("Error while trying to write onto bus: " + e.getMessage());
        }
    }

    @Override
    public EventInfo requestMessage(ComObject communicationObject) {
        if (this.pc == null) {
            return new EventInfo("Error while trying to read from bus: BusConnector not initialized correctly, ProcessCommunicator was null!");
        }
        try {
            EventInfo event = new EventInfo(
                "OUTGOING", 
                "READ.REQUEST", 
                this.tunnelingAddress, 
                communicationObject.getAddress().toString(), 
                new byte[]{}
            );
            if (event.isValid && monitorActive && monitor != null) {
                monitor.addToBuffer(event);
            }
            String value = this.pc.read(communicationObject.getDatapoint());
            return new EventInfo(
                "INCOMING", 
                "READ.RESPONSE", 
                "", 
                communicationObject.getAddress().toString(), 
                ComObject.stringToBytes(value, communicationObject.getDPT())
            );
        } catch(KNXException | InterruptedException e) {
            return new EventInfo("Error while trying to read from bus: " + e.getMessage());
        }
    }
}
