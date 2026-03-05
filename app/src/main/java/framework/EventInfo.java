package framework;

import java.time.LocalDateTime;
import java.util.HexFormat;

public final class EventInfo {
    public final String direction;
    public final String eventType;
    public final LocalDateTime timestamp;
    public final byte[] data;
    public final String destinationAddress;
    public final String sourceAddress;

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
    }

    @Override
    public String toString() {
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

    public static void printTemplate() {
        System.out.println("=================== Bus Monitor ======================");
        System.out.println(String.format("%-29s : Service  : SourceAddress : DestinationAddress : %-19s : Info", "Time", "Type"));
    }
}