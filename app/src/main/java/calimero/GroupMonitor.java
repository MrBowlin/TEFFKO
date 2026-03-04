package calimero;

/*
    Calimero 3 - A library for KNX network access
    Copyright (c) 2015, 2025 B. Malinowsky

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.util.HexFormat;

import io.calimero.DetachEvent;
import io.calimero.KNXException;
import io.calimero.link.KNXNetworkLink;
import io.calimero.link.KNXNetworkLinkIP;
import io.calimero.link.medium.TPSettings;
import io.calimero.process.ProcessCommunicator;
import io.calimero.process.ProcessCommunicatorImpl;
import io.calimero.process.ProcessEvent;
import io.calimero.process.ProcessListener;

public class GroupMonitor implements ProcessListener {
	private static final String remoteHost = "169.254.30.15";

	public static void main(final String[] args) {
		new GroupMonitor().run();
	}

	public void run() {
		final var anyLocal = new InetSocketAddress(0);
		final var remote = new InetSocketAddress(remoteHost, 3671);
		try (KNXNetworkLink knxLink = KNXNetworkLinkIP.newTunnelingLink(anyLocal, remote, false, new TPSettings());
		     ProcessCommunicator pc = new ProcessCommunicatorImpl(knxLink)) {

			pc.addProcessListener(this);
			System.out.println("Monitoring KNX network using KNXnet/IP server " + remoteHost + " ...");

			while (knxLink.isOpen()) Thread.sleep(1000);
		}
		catch (final KNXException | InterruptedException | RuntimeException e) {
			System.err.println(e);
		}
	}

	@Override
	public void groupWrite(final ProcessEvent e) { print("write.ind", e); }
	@Override
	public void groupReadRequest(final ProcessEvent e) { print("read.req", e); }
	@Override
	public void groupReadResponse(final ProcessEvent e) { print("read.res", e); }
	@Override
	public void detached(final DetachEvent e) {}

	private static void print(final String svc, final ProcessEvent e) {
		try {
			System.out.println(LocalTime.now() + " " + e.getSourceAddr() + "->" + e.getDestination() + " " + svc
					+ ": " + HexFormat.of().formatHex(e.getASDU()));
		}
		catch (final RuntimeException ex) {
			System.err.println(ex);
		}
	}
}