package calimero;

import java.net.InetSocketAddress;

import io.calimero.KNXException;
import io.calimero.knxnetip.KNXnetIPConnection;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;

public class CalimeroIpTest {
	private static final InetSocketAddress local = new InetSocketAddress(0);

	private static final InetSocketAddress server = new InetSocketAddress("169.254.30.15", KNXnetIPConnection.DEFAULT_PORT);

	public static void main(final String[] args) {
		System.out.println("Establish a tunneling connection to the KNXnet/IP server " + server);
		try (var knxLink = KNXNetworkLinkIP.newTunnelingLink(local, server, false, new TPSettings())) {
			System.out.println("Connection established to server " + knxLink.getName());
			GroupMonitor groupMonitor = new GroupMonitor();
			groupMonitor.run();
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error creating KNXnet/IP tunneling link: " + e);
		}
	}
}
