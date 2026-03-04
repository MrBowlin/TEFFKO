package framework;

public class TestRunner {
    public void run(Test test) {
        long startTime = System.currentTimeMillis();
        TestResult testResult = new TestResult();
        test.run(testResult);
        long endTime = System.currentTimeMillis();
        System.out.println(ANSI_BOLD + "================== Start of Test =====================" + ANSI_RESET);
        if (testResult.errorCount() > 0) {
            System.out.println(ANSI_RED + "Errors:");
            for (String error : testResult.errorList) {
                System.err.println(error);
            }
            System.out.println(ANSI_RESET);
        }
        if (testResult.failureCount() > 0) {
            System.out.println(ANSI_YELLOW + "Failures:");
            for (String error : testResult.errorList) {
                System.err.println(error);
            }
            System.out.println(ANSI_RESET);
        }
        System.out.println("Result:");
        System.out.println(String.format("%-25s %s", "Total Number of Tests: ", testResult.runCount()));
        if (testResult.successCount() > 0) System.out.print(ANSI_GREEN + ANSI_BOLD);
        System.out.println(String.format("%-25s %s", "Successfull Tests: ", testResult.successCount()) + ANSI_RESET);
        if (testResult.errorCount() > 0) System.out.print(ANSI_RED + ANSI_BOLD);
        System.out.println(String.format("%-25s %s", "Errored Tests: ", testResult.errorCount()) + ANSI_RESET);
        if (testResult.failureCount() > 0) System.out.print(ANSI_YELLOW + ANSI_BOLD);
        System.out.println(String.format("%-25s %s", "Failed Tests: ", testResult.failureCount()) + ANSI_RESET);
        System.out.println(ANSI_GRAY + "Total amount of time passed: " + (endTime - startTime) + "ms." + ANSI_RESET);
        System.out.println(ANSI_BOLD + "================== End of Test =======================" + ANSI_RESET);
    }

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.run(new Example());
    }

    public static final String ANSI_RESET       = "\u001B[0m";
    public static final String ANSI_BOLD        = "\u001B[1m";
    public static final String ANSI_UNDERSCORE  = "\u001B[4m";
    public static final String ANSI_RED         = "\u001B[31m";
    public static final String ANSI_GREEN       = "\u001B[32m";
    public static final String ANSI_YELLOW      = "\u001B[33m";
    public static final String ANSI_WHITE       = "\u001B[33m";
    public static final String ANSI_GRAY        = "\u001B[90m";
}
