package tests;

import java.lang.reflect.Method;

public abstract class TestCase implements Test, TestFixture {
    protected TestResult testResult;
    protected String lastMethodCalled = "";

    @Override
    public void run(TestResult testResult) {
        this.testResult = testResult;
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(UnitTest.class)) {
                try {
                    setUp(method);
                    lastMethodCalled = method.getName();
                    method.invoke(this);
                    tearDown(method);
                } catch (Exception e) {
                    testResult.addFailure(e.getMessage(), method.getName());
                }
            }
        }
    }

    @Override
    public void setUp(Method method) {
        
    }

    @Override
    public void tearDown(Method method) {
        
    }

    protected void pause(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println("Failed to wait: "+ e.getMessage());
        }
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

    protected void assertEquals(boolean  expected, boolean actual) {
        assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected void assertEquals(String expected, String actual) {
        assertTrue(expected.equals(actual), "Expected '" + expected + "' but got '" + actual + "' instead.");
    }
    
    protected void assertEquals(byte[] expected, byte[] actual) {
        for (int i=0; i<actual.length; i++) {
            assertTrue(expected[i] == actual[i], "Expected " + expected[i] + " but got " + actual[i] + " instead in ByteArray at position " + i + ".");
        }
    }

    protected void assertEquals(int expected, int actual) {
        assertTrue(expected == actual, "Expected " + expected + " but got " + actual + " instead.");
    }

    protected void assertEquals(double expected, double actual) {
        assertTrue(expected == actual, "Expected " + expected + " but got " + actual + " instead.");
    }
    
    protected void assertEquals(long expected, long actual) {
        assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected void assertEquals(float expected, float actual) {
        assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected void assertSmaller(int maximum, int actual) {
        assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected void assertSmaller(double maximum, double actual) {
        assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected void assertSmaller(long maximum, long actual) {
        assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected void assertSmaller(float maximum, float actual) {
        assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected void assertBigger(int minimum, int actual) {
        assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected void assertBigger(double minimum, double actual) {
        assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected void assertBigger(long minimum, long actual) {
        assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected void assertBigger(float minimum, float actual) {
        assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected void assertEquals(int expected, int actual, int tolerance) {
        assertTrue(expected <= actual - tolerance, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        assertTrue(expected >= actual + tolerance, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
    }

    protected void assertEquals(double expected, double actual, double tolerance) {
        assertTrue(expected <= actual - tolerance, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        assertTrue(expected >= actual + tolerance, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
    }

    protected void assertEquals(long expected, long actual, long tolerance) {
        assertTrue(expected <= actual - tolerance, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        assertTrue(expected >= actual + tolerance, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
    }

    protected void assertEquals(float expected, float actual, float tolerance) {
        assertTrue(expected <= actual - tolerance, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        assertTrue(expected >= actual + tolerance, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
    }
}
