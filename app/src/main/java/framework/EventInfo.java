package framework;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;

public final class EventInfo {
    public final boolean isValid;
    public final String direction;
    public final String eventType;
    public final LocalDateTime timestamp;
    public final byte[] data;
    public final String destinationAddress;
    public final String sourceAddress;
    public final String errorMessage;

    public EventInfo(
        String direction,
        String eventType,
        String sourceAddress,
        String destinationAddress,
        LocalDateTime timestamp,
        byte[] data
    ) {
        this.direction = direction;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.data = data;
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
        this.isValid = true;
        this.errorMessage = "";
    }

    public EventInfo(
        String direction,
        String eventType,
        String sourceAddress,
        String destinationAddress,
        byte[] data
    ) {
        this.direction = direction;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.data = data;
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
        this.isValid = true;
        this.errorMessage = "";
    }

    public EventInfo(
        String errorMessage
    ) {
        this.isValid = false;
        this.errorMessage = errorMessage;
        this.direction = null;
        this.eventType = null;
        this.timestamp = null;
        this.data = null;
        this.destinationAddress = null;
        this.sourceAddress = null;
    }

    public long getTimeDifference(EventInfo other) {
        if (!this.isValid || !other.isValid) {
            System.err.println("At least one of the EventInfo-Objects is not valid and does not contain a timestamp.");
        }
        long difference = ChronoUnit.MILLIS.between(this.timestamp, other.timestamp);
        return Math.abs(difference);
    }

    @Override
    public String toString() {
        if (!this.isValid) {
            return "ERROR: Busdata not valid: " + this.errorMessage;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%-29s", this.timestamp.toString()));
        stringBuilder.append(" : ");
        switch (this.direction) {
            case "INCOMING" -> stringBuilder.append("from Bus : ");
            case "OUTGOING" -> stringBuilder.append("to Bus   : ");
            default -> stringBuilder.append("unknown  : ");
        }
        stringBuilder.append(String.format("%-13s", this.sourceAddress));
        stringBuilder.append(" : ");
        stringBuilder.append(String.format("%-18s", this.destinationAddress));
        stringBuilder.append(" : ");
        switch (this.eventType) {
            case "WRITE.INDICATION" -> stringBuilder.append("GroupValue_Write    : ");
            case "READ.REQUEST" -> stringBuilder.append("GroupValue_Read     : ");
            case "READ.RESPONSE" -> stringBuilder.append("GroupValue_Response : ");
            default -> stringBuilder.append("Unknown_Type : ");
        }
        stringBuilder.append(HexFormat.of().formatHex(this.data));
        return stringBuilder.toString();
    }

    public static void printTemplate(String functionName) {
        System.out.println("=================== Bus Monitor ======================");
        System.out.println("For function: " + functionName);
        System.out.println(String.format("%-29s : Service  : SourceAddress : DestinationAddress : %-19s : Info", "Time", "Type"));
    }

    public static void printTemplate() {
        System.out.println("=================== Bus Monitor ======================");
        System.out.println(String.format("%-29s : Service  : SourceAddress : DestinationAddress : %-19s : Info", "Time", "Type"));
    }
}