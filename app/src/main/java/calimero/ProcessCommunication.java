package calimero;

import java.net.InetSocketAddress;

import io.calimero.GroupAddress;
import io.calimero.KNXException;
import io.calimero.datapoint.StateDP;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;

public class ProcessCommunication
{
	private static final String remoteHost = "169.254.30.15";

	private static final String group1 = "0/0/4";
	private static final String group2 = "0/0/4";

	public static void main(final String[] args)
	{
		final var anyLocal = new InetSocketAddress(0);
		final var remote = new InetSocketAddress(remoteHost, 3671);
		try (
            KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, new TPSettings());
			ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)
        ) {
			StateDP dp1 = new StateDP(new GroupAddress(group1), group1);
			pc.write(dp1, "01");
			StateDP dp2 = new StateDP(new GroupAddress(group2), group2);
			String value = pc.read(dp2);
			System.out.println("value is: " + value);
		}
		catch (KNXException | InterruptedException e) {
			System.out.println("Error accessing KNX datapoint: " + e.getMessage());
		}
	}
}
