package framework;

import java.lang.reflect.Method;

public abstract class KnxTestCase extends TestCase{

    protected IpBusConnector knx;
    
    @Override
    public void setUp(Method method) {
        if (method.isAnnotationPresent(KnxIpTest.class)) {
            KnxIpTest knxTest = method.getAnnotation(KnxIpTest.class);
            this.knx = new IpBusConnector(knxTest.remoteHost());
            this.knx.openConnection();
            this.knx.startMonitor();
        }
    }

    @Override
    public void tearDown(Method method) {
        if (method.isAnnotationPresent(KnxIpTest.class)) {
            KnxIpTest knxTest = method.getAnnotation(KnxIpTest.class);
            this.knx.stopMonitor();
            if (knxTest.busMonitor() == true) {
                this.knx.getMonitorBuffer().print(this.lastMethodCalled);
            }
            this.knx.closeConnection();
            this.knx = null;
        }
    }
}
