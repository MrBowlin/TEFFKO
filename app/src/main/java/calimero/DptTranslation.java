package calimero;

import java.util.HexFormat;

import io.calimero.KNXException;
import io.calimero.KNXFormatException;
import io.calimero.dptxlator.DPTXlator;
import io.calimero.dptxlator.DPTXlator2ByteFloat;
import static io.calimero.dptxlator.DPTXlator2ByteFloat.DPT_TEMPERATURE;
import io.calimero.dptxlator.TranslatorTypes;

public class DptTranslation
{
	public static void main(final String[] args) throws KNXException
	{
		// our knx data (DPT 9.001) we want to translate to a java temperature value
		final byte[] data = new byte[] { 0xc, (byte) 0xe2 };

		// Approach 1: manually create a DPT translator
		manualTranslation(data);

		// Approach 2: request DPT translator using factory method and DPT
		createUsingDpt(data);

		// Approach 3: use a datapoint model with a datapoint configuration
		//useDatapointModel(data);
	}

	private static void manualTranslation(final byte[] data) throws KNXFormatException
	{
		// DPT translator 9.001 for knx temperature datapoint
		final DPTXlator t = new DPTXlator2ByteFloat(DPT_TEMPERATURE);

		// translate knx data to java value
		t.setData(data);
		final double temperature = t.getNumericValue();
		final String formatted = t.getValue();
		System.out.println("temperature is " + formatted + " (" + temperature + ")");

		// set temperature value of -4 degree celsius (physical unit can be omitted)
		t.setValue("-4 °C");
		// get KNX translated data
		System.out.println(t.getValue() + " translated to knx data: 0x" + HexFormat.of().formatHex(t.getData()));
	}

	private static void createUsingDpt(final byte[] data) throws KNXException
	{
		final DPTXlator t = TranslatorTypes.createTranslator(DPT_TEMPERATURE.getID(), data);
		System.out.println("temperature is " + t.getValue() + " (" + t.getNumericValue() + ")");
	}
}
