package tests;

import io.calimero.GroupAddress;
import io.calimero.KNXException;
import io.calimero.datapoint.Datapoint;
import io.calimero.datapoint.StateDP;
import io.calimero.dptxlator.DPT;

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
}
