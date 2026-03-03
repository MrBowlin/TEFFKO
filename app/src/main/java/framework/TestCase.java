package framework;

import java.lang.reflect.Method;

public abstract class TestCase implements Test, TestFixture {
    private TestResult testResult;

    @Override
    public void run(TestResult testResult) {
        this.testResult = testResult;
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(UnitTest.class)) {
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setUp() {

    }

    @Override
    public void tearDown() {
        
    }

    protected void assertTrue(boolean condition, String message) {
        try {
            if (condition) {
                testResult.addSuccess();
            } else {
                testResult.addError(message);
            }
        } catch (Exception i) {
            System.err.println("TestCase has not been initialized yet.");
        }
    }
}
