package framework;

import java.time.LocalDate;
import java.time.LocalTime;

public class ResponseObject {
    public final boolean isValid;
    public final byte[] data;
    public final String destinationAddress;
    public final String sourceAddress;
    public final String messageType;
    public final LocalTime creationTime;
    public final LocalDate creationDate;
    public final String errorMessage;

    public ResponseObject(
        byte[] data,
        String destinationAddress,
        String sourceAddress,
        String messageType,
        LocalTime creationTime,
        LocalDate creationDate
    ) {
        this.isValid = true;
        this.data = data;
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
        this.messageType = messageType;
        this.creationTime = creationTime;
        this.creationDate = creationDate;
        this.errorMessage = "";
    }
    
    public ResponseObject(String errorMessage) {
        this.isValid = false;
        this.data = null;
        this.destinationAddress = "";
        this.sourceAddress = "";
        this.messageType = "";
        this.creationTime = null;
        this.creationDate = null;
        this.errorMessage = errorMessage;
    }
}
