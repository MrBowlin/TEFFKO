package tests;

import java.lang.reflect.Method;

public abstract class KnxTestCase extends TestCase{

    protected BusConnector knx;
    
    @Override
    public void setUp(Method method) {
        if (method.isAnnotationPresent(KnxTest.class)) {
            KnxTest knxTest = method.getAnnotation(KnxTest.class);
            this.knx = new BusConnector(knxTest.remoteHost());
            this.knx.openConnection();
            this.knx.startMonitor();
        }
    }

    @Override
    public void tearDown(Method method) {
        if (method.isAnnotationPresent(KnxTest.class)) {
            KnxTest knxTest = method.getAnnotation(KnxTest.class);
            this.knx.stopMonitor();
            if (knxTest.busMonitor() == true) {
                this.knx.getMonitorBuffer().print(this.lastMethodCalled);
            }
            this.knx.closeConnection();
            this.knx = null;
        }
    }
}
