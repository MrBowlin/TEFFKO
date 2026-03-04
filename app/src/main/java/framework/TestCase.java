package framework;

import java.lang.reflect.Method;

public abstract class TestCase implements Test, TestFixture {
    private TestResult testResult;
    private String lastMethodCalled = "";

    @Override
    public void run(TestResult testResult) {
        this.testResult = testResult;
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(UnitTest.class)) {
                try {
                    lastMethodCalled = method.getName();
                    method.invoke(this);
                } catch (Exception e) {
                    testResult.addFailure(e.getMessage(), method.getName());
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
                testResult.addError(message, lastMethodCalled);
            }
        } catch (Exception i) {
            System.err.println("TestCase has not been initialized yet.");
        }
    }

    protected void assertTrue(boolean condition) {
        assertTrue(condition, "Expected 'true' but got '" + condition + "' instead.");
    }

    protected void assertFalse(boolean condition) {
        assertTrue(!condition, "Expected 'false' but got '" + condition + "' instead.");
    }

    protected void assertInt(int expected, int actual) {
        assertTrue(expected == actual, "Expected " + expected + " but got " + actual + " instead.");
    }

    protected void assertBool(boolean  expected, boolean actual) {
        assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected void assertDouble(double expected, double actual) {
        assertTrue(expected == actual, "Expected " + expected + " but got " + actual + " instead.");
    }

    protected void assertString(String expected, String actual) {
        assertTrue(expected.equals(actual), "Expected '" + expected + "' but got '" + actual + "' instead.");
    }
    
    protected void assertValue(Byte[] expected, Byte[] actual) {
        for (int i=0; i<actual.length; i++) {
            assertTrue(expected[i].compareTo(actual[i]) == 0, "Expected " + expected[i] + " but got " + actual[i] + " instead in ByteArray at position " + i + ".");
        }
    }
}
