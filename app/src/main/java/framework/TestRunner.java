package framework;

public class TestRunner {
    public void run(Test test) {
        TestResult testResult = new TestResult();
        test.run(testResult);
        System.out.println("----------------- Start of Test ----------------------");
        if (testResult.errorCount() > 0) {
            System.out.println("Errors:");
            for (String error : testResult.errorList) {
                System.err.println(error);
            }
            System.out.println("");
        }
        System.out.println("Result:");
        System.out.println("Total Number of Tests: " + testResult.runCount());
        System.out.println("Successfull Tests: " + testResult.successCount());
        System.out.println("Failed Tests: " + testResult.errorCount());
        System.out.println("------------------ End of Test -----------------------");
    }

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.run(new Example());
    }
}
