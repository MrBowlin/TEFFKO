package framework;

import java.util.ArrayList;

public class Example extends TestCase{

    @UnitTest
    public void testfun() {
        BusConnector connector = new BusConnector("169.254.30.15");
        connector.openConnection();
        connector.startListener();
        connector.readBoolean("0/0/5");
        pause(1000);
        connector.stopListener();
        ArrayList<EventInfo> buffer = connector.getMessageBuffer();
        connector.closeConnection();
        EventInfo.printTemplate();
        for (EventInfo event : buffer) {
            System.out.println(event);
        }
    }
}
