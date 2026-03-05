package framework;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import io.calimero.GroupAddress;
import io.calimero.KNXException;
import io.calimero.datapoint.Datapoint;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;

public class BusConnector {

    private BusListener busListener;
    private KNXNetworkLink knxLink;
    private ProcessCommunicator pc;

    final private InetSocketAddress anyLocal;
    final private InetSocketAddress remote; 
    final private TPSettings tpSettings;

    public BusConnector(String remoteHost) {
        this.anyLocal = new InetSocketAddress(0);
        this.remote = new InetSocketAddress(remoteHost, 3671);
        this.tpSettings = new TPSettings();
    }

    public void openConnection() {
        try {
            this.knxLink = KNXNetworkLinkIP.newTunnelingLink(this.anyLocal, this.remote, false, this.tpSettings);
            this.pc = new ProcessCommunicatorImpl(this.knxLink);
            this.busListener = new BusListener();
        } catch (KNXException | InterruptedException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            this.pc.close();
            this.knxLink.close();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.err.println("Error closing KNX datapoint: " + e.getMessage());
        }
    }

    public ArrayList<EventInfo> getMessageBuffer() {
        if (this.busListener != null) {
            return busListener.getBuffer();
        } else {
            System.err.println("Could not retrieve Buffer, BusListener is null.");
            return new ArrayList<>();
        }
    }

    public void startListener() {
        if (this.pc != null && this.busListener != null) {
            try {
                this.pc.addProcessListener(this.busListener);
                this.busListener.clearBuffer();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                 System.err.println("Error starting KNX Listener: " + e.getMessage());
            }
        }
        else {
            System.err.println("Could not start BusListener. Either ProcessCommunicator or BusListener is null.");
        }
    }

    public void stopListener() {
        if (this.pc != null && this.busListener != null) {
            this.pc.removeProcessListener(this.busListener);
        }
        else {
            System.err.println("Could not stop BusListener. Either ProcessCommunicator or BusListener is null.");
        }
    }

    public void writeBoolean(boolean value, String groupAddress) {
        try {
            this.pc.write(new GroupAddress(groupAddress), value);
        } catch(KNXException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
    }

    public void writeString(String value, String groupAddress) {
        try {
            this.pc.write(new GroupAddress(groupAddress), value);
        } catch(KNXException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
    }

    public void writeDatapoint(String value, Datapoint dp) {
        try {
            this.pc.write(dp, value);
        } catch(KNXException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
    }

    public boolean readBoolean(String groupAddress) {
        try {
            boolean value = this.pc.readBool(new GroupAddress(groupAddress));
            return value;
        } catch(KNXException | InterruptedException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
        return false;
    }

    public String readString(String groupAddress) {
        try {
            String value = this.pc.readString(new GroupAddress(groupAddress));
            return value;
        } catch(KNXException | InterruptedException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
        return "";
    }

    public String readDatapoint(Datapoint dp) {
        try {
            String value = this.pc.read(dp);
            return value;
        } catch(KNXException | InterruptedException e) {
            System.err.println("Error accessing KNX datapoint: " + e.getMessage());
        }
        return "";
    }
}
