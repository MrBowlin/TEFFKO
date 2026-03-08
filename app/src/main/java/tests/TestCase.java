package tests;

import java.lang.reflect.InvocationTargetException;
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
                } catch (IllegalAccessException | InvocationTargetException e) {
                    testResult.addFailure(e.getMessage(), method.getName());
                } finally {
                    tearDown(method);
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

    protected boolean assertTrue(boolean condition, String message) {
        try {
            if (condition) {
                testResult.addSuccess();
            } else {
                testResult.addError(message, lastMethodCalled);
            }
        } catch (Exception i) {
            System.err.println("TestCase has not been initialized yet.");
        }
        return condition;
    }

    protected boolean assertTrue(boolean condition) {
        return assertTrue(condition, "Expected 'true' but got '" + condition + "' instead.");
    }

    protected boolean assertFalse(boolean condition) {
        return assertTrue(!condition, "Expected 'false' but got '" + condition + "' instead.");
    }

    protected boolean assertEquals(boolean  expected, boolean actual) {
        return assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected boolean assertEquals(String expected, String actual) {
        return assertTrue(expected.equals(actual), "Expected '" + expected + "' but got '" + actual + "' instead.");
    }
    
    protected boolean assertEquals(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            assertTrue(false, "Length of byte-Arrays differs!");
        } else {
            for (int i=0; i<actual.length; i++) {
                if (expected[i] != actual[i]) 
                    return assertTrue(false, "Expected " + expected[i] + " but got " + actual[i] + " instead in ByteArray at position " + i + ".");
            }
        }
        return assertTrue(true);
    }

    protected boolean assertEquals(int expected, int actual) {
        return assertTrue(expected == actual, "Expected " + expected + " but got " + actual + " instead.");
    }

    protected boolean assertEquals(double expected, double actual) {
        return assertTrue(expected == actual, "Expected " + expected + " but got " + actual + " instead.");
    }
    
    protected boolean assertEquals(long expected, long actual) {
        return assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected boolean assertEquals(float expected, float actual) {
        return assertTrue(expected == actual, "Expected '" + expected + "' but got '" + actual + "' instead.");
    }

    protected boolean assertSmaller(int maximum, int actual) {
        return assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected boolean assertSmaller(double maximum, double actual) {
        return assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected boolean assertSmaller(long maximum, long actual) {
        return assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected boolean assertSmaller(float maximum, float actual) {
        return assertTrue(maximum > actual, "Value '" + actual + "' was bigger than '" + maximum + "'.");
    }

    protected boolean assertBigger(int minimum, int actual) {
        return assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected boolean assertBigger(double minimum, double actual) {
        return assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected boolean assertBigger(long minimum, long actual) {
        return assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected boolean assertBigger(float minimum, float actual) {
        return assertTrue(minimum < actual, "Value '" + actual + "' was bigger than '" + minimum + "'.");
    }

    protected boolean assertEquals(int expected, int actual, int tolerance) {
        boolean condition = expected <= actual - tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        condition = expected >= actual + tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
        return assertTrue(true);
    }

    protected boolean assertEquals(double expected, double actual, double tolerance) {
        boolean condition = expected <= actual - tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        condition = expected >= actual + tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
        return assertTrue(true);
    }

    protected boolean assertEquals(long expected, long actual, long tolerance) {
        boolean condition = expected <= actual - tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        condition = expected >= actual + tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
        return assertTrue(true);
    }

    protected boolean assertEquals(float expected, float actual, float tolerance) {
        boolean condition = expected <= actual - tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was smaller than '" + expected + "' with tolerance of '" + tolerance + "'.");
        condition = expected >= actual + tolerance;
        if (!condition) return assertTrue(false, "Value '" + actual + "' was bigger than '" + expected + "' with tolerance of '" + tolerance + "'.");
        return assertTrue(true);
    }
}
