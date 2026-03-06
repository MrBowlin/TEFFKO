package framework;

import java.lang.reflect.Method;

public abstract class KnxTestCase extends TestCase{

    protected BusConnector knxConnector;
    
    @Override
    public void run(TestResult testResult) {
        this.testResult = testResult;
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(KnxTest.class)) {
                try {
                    lastMethodCalled = method.getName();
                    KnxTest knxTest = method.getAnnotation(KnxTest.class);
                    this.knxConnector = new BusConnector(knxTest.remoteHost());
                    this.knxConnector.openConnection();
                    this.knxConnector.startListener();
                    method.invoke(this);
                    this.knxConnector.stopListener();
                    if (knxTest.busMonitor() == true) {
                        this.knxConnector.getMessageBuffer().print(lastMethodCalled);
                    }
                    this.knxConnector.closeConnection();
                    this.knxConnector = null;
                } catch (Exception e) {
                    testResult.addFailure(e.getMessage(), method.getName());
                }
            }
        }
    }
}
