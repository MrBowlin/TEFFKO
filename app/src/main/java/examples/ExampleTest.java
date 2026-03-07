package examples;

import io.calimero.dptxlator.DPTXlatorBoolean;
import tests.ComObject;
import tests.EventInfo;
import tests.KnxTest;
import tests.KnxTestCase;
import tests.UnitTest;

public class ExampleTest extends KnxTestCase {

    @UnitTest
    @KnxTest(busMonitor=true)
    public void testfun() {
        ComObject eingang1 = new ComObject("0/0/3", DPTXlatorBoolean.DPT_SWITCH);
        ComObject eingang2 = new ComObject("0/0/4", DPTXlatorBoolean.DPT_SWITCH);
        ComObject ausgang = new ComObject("0/0/5", DPTXlatorBoolean.DPT_SWITCH);

        knx.writeMessage(eingang1, "off");
        knx.writeMessage(eingang2, "off");

        EventInfo event = knx.awaitMessage(ausgang, 1000);
        String response = knx.bytesToString(event.data, DPTXlatorBoolean.DPT_SWITCH);
        assertEquals("off", response);

        knx.writeMessage(eingang1, "on");
        EventInfo sendEvent = knx.writeMessage(eingang2, "on");

        event = knx.awaitMessage(ausgang, 1000);
        response = knx.bytesToString(event.data, DPTXlatorBoolean.DPT_SWITCH);
        assertEquals("on", response);

        long timeDifference = sendEvent.getTimeDifference(event);
        assertSmaller(500, timeDifference);
    }
}
