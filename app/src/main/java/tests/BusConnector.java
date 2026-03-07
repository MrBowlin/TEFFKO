package tests;

import java.net.InetSocketAddress;

import io.calimero.KNXException;
import io.calimero.dptxlator.DPT;
import io.calimero.dptxlator.DPTXlator;
import io.calimero.dptxlator.TranslatorTypes;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;

public class BusConnector {

    private BusMonitor monitor;
    private KNXNetworkLink knxLink;
    private ProcessCommunicator pc;
    private boolean monitorActive;

    final private InetSocketAddress anyLocal;
    final private InetSocketAddress remote; 
    final private TPSettings tpSettings;
    private String tunnelingAddress;

    public BusConnector(String remoteHost) {
        this.anyLocal = new InetSocketAddress(0);
        this.remote = new InetSocketAddress(remoteHost, 3671);
        this.tpSettings = new TPSettings();
        monitorActive = false;
    }

    public byte[] stringToBytes(String value, DPT dataPointType) {
        try {
            DPTXlator translator = TranslatorTypes.createTranslator(dataPointType);
            translator.setValue(value);
            return translator.getData();
        } catch (KNXException e) {
            System.err.println(e.getMessage());
            return new byte[]{};
        }
    }

    public String bytesToString(byte[] data, DPT dataPointType) {
        try {
            DPTXlator translator = TranslatorTypes.createTranslator(dataPointType);
            translator.setData(data);
            return translator.getValue();
        } catch (KNXException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    public void openConnection() {
        try {
            this.knxLink = KNXNetworkLinkIP.newTunnelingLink(this.anyLocal, this.remote, false, this.tpSettings);
            this.tunnelingAddress = this.knxLink.getKNXMedium().assignedAddress().get().toString();
            this.pc = new ProcessCommunicatorImpl(this.knxLink);
            this.monitor = new BusMonitor();
            monitorActive = true;
        } catch (KNXException | InterruptedException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
    }

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

    public EventBuffer getMonitorBuffer() {
        if (this.monitor != null) {
            return monitor.getBuffer();
        } else {
            System.err.println("Could not retrieve Buffer, BusMonitor is null.");
            return new EventBuffer();
        }
    }

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

    public void stopMonitor() {
        if (this.pc != null && this.monitor != null) {
            this.pc.removeProcessListener(this.monitor);
        }
        else {
            System.err.println("Could not stop BusListener. Either ProcessCommunicator or BusListener is null.");
        }
    }

    public EventInfo awaitMessage(ComObject communicationObject, long timeout) {
        if (this.pc == null) {
            return new EventInfo("Error while waiting for message: BusConnector not initialized correctly, ProcessCommunicator was null!");
        }
        BusListener listener = new BusListener();
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
                stringToBytes(value, communicationObject.getDPT())
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

    public EventInfo readRequest(ComObject communicationObject) {
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
                stringToBytes(value, communicationObject.getDPT())
            );
        } catch(KNXException | InterruptedException e) {
            return new EventInfo("Error while trying to read from bus: " + e.getMessage());
        }
    }
}
