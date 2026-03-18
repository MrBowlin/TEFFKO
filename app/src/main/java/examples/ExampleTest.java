package examples;

import framework.ComObject;
import framework.EventInfo;
import framework.KnxIpTest;
import framework.KnxTestCase;
import framework.UnitTest;
import io.calimero.dptxlator.DPTXlatorBoolean;

public class ExampleTest extends KnxTestCase {

    @UnitTest
    @KnxIpTest(busMonitor=true)
    public void testfun() {
        ComObject eingang1 = new ComObject("0/0/3", DPTXlatorBoolean.DPT_SWITCH);
        ComObject eingang2 = new ComObject("0/0/4", DPTXlatorBoolean.DPT_SWITCH);
        ComObject ausgang = new ComObject("0/0/5", DPTXlatorBoolean.DPT_SWITCH);

        knx.writeMessage(eingang1, "off");
        knx.writeMessage(eingang2, "off");

        EventInfo event = knx.awaitMessage(ausgang, 1000);
        String response = ausgang.bytesToString(event.data);
        assertEquals("off", response);

        knx.writeMessage(eingang1, "on");
        EventInfo sendEvent = knx.writeMessage(eingang2, "on");

        event = knx.awaitMessage(ausgang, 1000);
        response = ausgang.bytesToString(event.data);
        assertEquals("on", response);

        long timeDifference = sendEvent.getTimeDifference(event);
        assertSmaller(500, timeDifference);
    }

    @UnitTest
    @KnxIpTest(busMonitor=true)
    public void testReadRequest() {
        ComObject eingang1 = new ComObject("0/0/3", DPTXlatorBoolean.DPT_SWITCH);
        ComObject eingang2 = new ComObject("0/0/4", DPTXlatorBoolean.DPT_SWITCH);
        ComObject ausgang = new ComObject("0/0/5", DPTXlatorBoolean.DPT_SWITCH);

        knx.writeMessage(eingang1, "off");
        knx.writeMessage(eingang2, "off");

        EventInfo event = knx.requestMessage(ausgang);
        byte[] expected = ausgang.stringToBytes("off");
        if (assertTrue(event.isValid)) {
            assertEquals(expected, event.data);
        }
    }
}
