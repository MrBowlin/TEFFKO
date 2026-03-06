package framework;

import java.util.ArrayList;

public class EventBuffer extends ArrayList<EventInfo>{

    public void print() {
        EventInfo.printTemplate();
        for (EventInfo event : this) {
            System.out.println(event.toString());
        }
    }

    public void print(String functionName) {
        EventInfo.printTemplate(functionName);
        for (EventInfo event : this) {
            System.out.println(event.toString());
        }
    }

    public boolean containsFromSource(String sourceAddress) {
        for (EventInfo event : this) {
            if (event.sourceAddress.equals(sourceAddress)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsToDestination(String destinationAddress) {
        for (EventInfo event : this) {
            if (event.destinationAddress.equals(destinationAddress)) {
                return true;
            }
        }
        return false;
    }

    public EventBuffer getSublistFromSource(String sourceAddress) {
        EventBuffer subBuffer = new EventBuffer();
        for (EventInfo event : this) {
            if (event.sourceAddress.equals(sourceAddress)) {
                subBuffer.add(event);
            }
        }
        if (!subBuffer.isEmpty())
            return subBuffer;
        else 
            return null;
    }

    public EventBuffer getSublistToDestination(String destinationAddress) {
        EventBuffer subBuffer = new EventBuffer();
        for (EventInfo event : this) {
            if (event.destinationAddress.equals(destinationAddress)) {
                subBuffer.add(event);
            }
        }
        if (!subBuffer.isEmpty())
            return subBuffer;
        else 
            return null;
    }
}

