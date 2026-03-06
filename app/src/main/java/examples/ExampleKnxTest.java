package examples;

import framework.EventBuffer;
import framework.EventInfo;
import framework.KnxTest;
import framework.KnxTestCase;

public class ExampleKnxTest extends KnxTestCase {

    @KnxTest(remoteHost="169.254.30.15", busMonitor=true)
    public void myFunction() {
        knxConnector.writeBoolean(false, "0/0/3");
        knxConnector.writeBoolean(false, "0/0/4");
        knxConnector.writeBoolean(true, "0/0/3");
        knxConnector.writeBoolean(true, "0/0/4");
        assertBool(true, knxConnector.readBoolean("0/0/5"));
        EventBuffer buffer = knxConnector.getMessageBuffer();
        if (buffer.containsToDestination("0/0/5")) {
            EventInfo event = buffer.getSublistToDestination("0/0/5").getLast();
            System.out.println(event.timestamp);
        }
    }
}