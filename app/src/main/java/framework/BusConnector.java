package framework;

public interface BusConnector {
    public void openConnection();
    public void closeConnection();
    public EventBuffer getMonitorBuffer();
    public void startMonitor();
    public void stopMonitor();
    public EventInfo awaitMessage(ComObject communicationObject, long timeout);
    public EventInfo writeMessage(ComObject communicationObject, String value);
    public EventInfo requestMessage(ComObject communicationObject);
}
