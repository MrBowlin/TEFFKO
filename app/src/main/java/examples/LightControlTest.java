package examples;

import framework.ComObject;
import framework.EventInfo;
import framework.KnxIpTest;
import framework.KnxTestCase;
import framework.UnitTest;
import io.calimero.dptxlator.DPTXlator2ByteUnsigned;
import io.calimero.dptxlator.DPTXlatorBoolean;

public class LightControlTest extends KnxTestCase {

    @UnitTest
    @KnxIpTest
    public void testBrightnessLoop() {
        // Definieren des Kommunikationsobjektes
        ComObject helligkeitsEingang = new ComObject("0/0/9", DPTXlatorBoolean.DPT_BOOL);

        // Warten auf wiederholende Nachrichten
        EventInfo event1 = knx.awaitMessage(helligkeitsEingang, 5000);
        EventInfo event2 = knx.awaitMessage(helligkeitsEingang, 5000);
        EventInfo event3 = knx.awaitMessage(helligkeitsEingang, 5000);

        // Überprüfen der empfangenen Nachrichten
        boolean result1 = assertTrue(event1.isValid);
        boolean result2 = assertTrue(event2.isValid);
        boolean result3 = assertTrue(event3.isValid);

        if (!result1 || !result2 || !result3) return;

        // Vergleich der zeitlichen Abstände
        long diff1 = event1.getTimeDifference(event2);
        long diff2 = event2.getTimeDifference(event3);
        
        assertEquals(5000, diff1, 100);
        assertEquals(5000, diff2, 100);
    }

    @UnitTest
    @KnxIpTest
    public void testStateChanges() {
        // Definieren des Kommunikationsobjektes
        ComObject helligkeitsEingang = new ComObject("0/0/8", DPTXlator2ByteUnsigned.DPT_BRIGHTNESS);
        ComObject melderEingang = new ComObject("0/0/9", DPTXlatorBoolean.DPT_BOOL);
        ComObject steuerungAusgang = new ComObject("0/0/10", DPTXlatorBoolean.DPT_ENABLE);
        EventInfo response;
        byte[] expected;

        // Herstellen des Ausgangszustands
        knx.writeMessage(melderEingang, "false");
        knx.writeMessage(helligkeitsEingang, "50");
        knx.awaitMessage(steuerungAusgang, 500);
        pause(10000);

        // Test 1: 
        knx.writeMessage(melderEingang, "true");
        response = knx.awaitMessage(steuerungAusgang, 500);
        expected = steuerungAusgang.stringToBytes("on");
        if (assertTrue(response.isValid)) 
            assertEquals(expected, response.data);

        // Test 2: 
        response = knx.awaitMessage(steuerungAusgang, 10500);
        expected = steuerungAusgang.stringToBytes("off");
        if (assertTrue(response.isValid)) 
            assertEquals(expected, response.data);

        // Test 3:
        knx.writeMessage(helligkeitsEingang, "500");
        knx.writeMessage(melderEingang, "true");
        knx.awaitMessage(steuerungAusgang, 500);
        assertFalse(response.isValid);

        // Test 4:
        knx.writeMessage(helligkeitsEingang, "50");
        knx.writeMessage(melderEingang, "true");
        knx.awaitMessage(steuerungAusgang, 500);
        knx.writeMessage(helligkeitsEingang, "500");

        response = knx.awaitMessage(steuerungAusgang, 10500);
        expected = steuerungAusgang.stringToBytes("off");
        if (assertTrue(response.isValid)) 
            assertEquals(expected, response.data);
    }
}
