package examples;

import framework.BusConnector;
import framework.EventBuffer;
import framework.TestCase;
import framework.UnitTest;

public class ExampleTest extends TestCase{

    @UnitTest
    public void testfun() {
        BusConnector connector = new BusConnector("169.254.30.15");
        connector.openConnection();
        connector.startListener();
        connector.readBoolean("0/0/5");
        pause(1000);
        connector.stopListener();
        EventBuffer buffer = connector.getMessageBuffer();
        connector.closeConnection();
        buffer.print();
    }
}
