package examples;

import framework.TestRunner;
import framework.TestSuite;

public class Main {
    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        TestSuite myTestSuite = new TestSuite();
        myTestSuite.addTest(new ExampleTest());
        myTestSuite.addTest(new AirConditionerTest());
        testRunner.run(myTestSuite);
    }
}
