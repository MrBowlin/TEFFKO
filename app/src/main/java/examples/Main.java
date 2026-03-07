package examples;

import tests.TestRunner;

public class Main {
    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.run(new ExampleTest());
    }
}
