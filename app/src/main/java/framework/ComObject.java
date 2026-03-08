package framework;

import io.calimero.GroupAddress;
import io.calimero.KNXException;
import io.calimero.datapoint.Datapoint;
import io.calimero.datapoint.StateDP;
import io.calimero.dptxlator.DPT;
import io.calimero.dptxlator.DPTXlator;
import io.calimero.dptxlator.TranslatorTypes;

public class ComObject {
    private DPT dataPointType;
    private GroupAddress groupAddress;
    private Datapoint dataPoint;

    public ComObject(String groupAddress, DPT dataPointType) {
        try {
            this.dataPointType = dataPointType;
            this.groupAddress = new GroupAddress(groupAddress);
            this.dataPoint = new StateDP(this.groupAddress, groupAddress, 0, dataPointType.getID());
        } catch (KNXException e) {
            System.err.println("Error creating KNX datapoint: " + e.getMessage());
            this.groupAddress = null;
            this.dataPoint = null;
            this.dataPointType = null;
        }
    }

    public DPT getDPT() {
        return this.dataPointType;
    }

    public GroupAddress getAddress() {
        return this.groupAddress;
    }

    public Datapoint getDatapoint() {
        return this.dataPoint;
    }

    public static byte[] stringToBytes(String value, DPT dataPointType) {
        try {
            DPTXlator translator = TranslatorTypes.createTranslator(dataPointType);
            translator.setValue(value);
            return translator.getData();
        } catch (KNXException e) {
            System.err.println(e.getMessage());
            return new byte[]{};
        }
    }

    public static String bytesToString(byte[] data, DPT dataPointType) {
        try {
            DPTXlator translator = TranslatorTypes.createTranslator(dataPointType);
            translator.setData(data);
            return translator.getValue();
        } catch (KNXException e) {
            System.err.println(e.getMessage());
            return "";
        }
    }
}
