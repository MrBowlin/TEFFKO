package examples;

import io.calimero.dptxlator.DPTXlator2ByteFloat;
import io.calimero.dptxlator.DPTXlatorBoolean;
import tests.ComObject;
import tests.EventInfo;
import tests.KnxTest;
import tests.KnxTestCase;
import tests.UnitTest;

public class AirConditionerTest extends KnxTestCase {

    @UnitTest
    @KnxTest(busMonitor=true)
    public void testTemperature() {
        // Definiere Kommunikationsobjekte
        ComObject eingang = new ComObject("0/0/6", DPTXlator2ByteFloat.DPT_TEMPERATURE);
        ComObject ausgang = new ComObject("0/0/7", DPTXlatorBoolean.DPT_SWITCH);
        EventInfo request;
        EventInfo response;
        byte[] expected;

        // Stelle Startzustand her
        knx.writeMessage(eingang, "0");
        knx.awaitMessage(ausgang, 500);

        // Test 1: Temperatur bleibt unter 25, Antwort "off" erwartet.
        request = knx.writeMessage(eingang, "20");
        response = knx.awaitMessage(ausgang, 500);
        expected = knx.stringToBytes("off", DPTXlatorBoolean.DPT_SWITCH);
        if (assertTrue(response.isValid)) {
            assertEquals(expected, response.data);
            assertSmaller(500, request.getTimeDifference(response));
        }

        // Test 2: Temperatur steigt über 25, Antwort "on" erwartet.
        request = knx.writeMessage(eingang, "30");
        response = knx.awaitMessage(ausgang, 500);
        expected = knx.stringToBytes("on", DPTXlatorBoolean.DPT_SWITCH);
        if (assertTrue(response.isValid)) {
            assertEquals(expected, response.data);
            assertSmaller(500, request.getTimeDifference(response));
        }

        // Test 3: Temperatur bleibt über 25, Antwort "on" erwartet.
        request = knx.writeMessage(eingang, "30");
        response = knx.awaitMessage(ausgang, 500);
        expected = knx.stringToBytes("on", DPTXlatorBoolean.DPT_SWITCH);
        if (assertTrue(response.isValid)) {
            assertEquals(expected, response.data);
            assertSmaller(500, request.getTimeDifference(response));
        }

        // Test 4: Temperatur sinkt unter 25, Antwort "off" erwartet.
        request = knx.writeMessage(eingang, "20");
        response = knx.awaitMessage(ausgang, 500);
        expected = knx.stringToBytes("off", DPTXlatorBoolean.DPT_SWITCH);
        if (assertTrue(response.isValid)) {
            assertEquals(expected, response.data);
            assertSmaller(500, request.getTimeDifference(response));
        }
    }
}
