package framework;

import java.net.InetSocketAddress;

import io.calimero.GroupAddress;
import io.calimero.KNXException;
import io.calimero.datapoint.Datapoint;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;

public class BusConnection {

    final InetSocketAddress anyLocal;
    final InetSocketAddress remote; 
    final BusListener busListener;
    final TPSettings tpSettings;

    public BusConnection(String remoteHost) {
        this.anyLocal = new InetSocketAddress(0);
        this.remote = new InetSocketAddress(remoteHost, 3671);
        busListener = new BusListener(remoteHost);
        tpSettings = new TPSettings();
    }

    public ResponseObject awaitDatapoint(String destinationAddress, long timeout) {
        return busListener.awaitMessage(destinationAddress, timeout);
    }

    public void writeBoolean(boolean value, String groupAddress) {
        try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, tpSettings);
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
            pc.write(new GroupAddress(groupAddress), value);
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
    }

    public void writeString(boolean value, String groupAddress) {
        try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, tpSettings);
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
            pc.write(new GroupAddress(groupAddress), value);
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
    }

    public void writeDatapoint(String value, Datapoint dp) {
        try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, tpSettings);
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
            pc.write(dp, value);
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
    }

    public boolean readBoolean(String groupAddress) {
        try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, tpSettings);
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
            boolean value = pc.readBool(new GroupAddress(groupAddress));
            return value;
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
        return false;
    }

    public String readString(String groupAddress) {
        try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, tpSettings);
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
            String value = pc.readString(new GroupAddress(groupAddress));
            return value;
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
        return "";
    }

    public String readDatapoint(Datapoint dp) {
        try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, tpSettings);
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink);
        ) {
            String value = pc.read(dp);
            return value;
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
        return "";
    }
}
