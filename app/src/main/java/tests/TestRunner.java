package tests;

public class TestRunner {
    public void run(Test test) {
        TestResult testResult = new TestResult();
        long startTime = System.currentTimeMillis();
        test.run(testResult);
        long endTime = System.currentTimeMillis();
        printResult(testResult, endTime - startTime);
    }

    private void printResult(TestResult testResult, long time) {
        System.out.println(AnsiFormatting.ANSI_BOLD + "================== Start of Test =====================" + AnsiFormatting.ANSI_RESET);
        if (testResult.errorCount() > 0) {
            System.out.println(AnsiFormatting.ANSI_RED + "Errors:");
            for (String error : testResult.errorList) {
                System.err.println(error);
            }
            System.out.println(AnsiFormatting.ANSI_RESET);
        }
        if (testResult.failureCount() > 0) {
            System.out.println(AnsiFormatting.ANSI_YELLOW + "Failures:");
            for (String error : testResult.errorList) {
                System.err.println(error);
            }
            System.out.println(AnsiFormatting.ANSI_RESET);
        }
        System.out.println("Result:");
        System.out.println(String.format("%-25s %s", "Total Number of Tests: ", testResult.runCount()));
        if (testResult.successCount() > 0) System.out.print(AnsiFormatting.ANSI_GREEN + AnsiFormatting.ANSI_BOLD);
        System.out.println(String.format("%-25s %s", "Successfull Tests: ", testResult.successCount()) + AnsiFormatting.ANSI_RESET);
        if (testResult.errorCount() > 0) System.out.print(AnsiFormatting.ANSI_RED + AnsiFormatting.ANSI_BOLD);
        System.out.println(String.format("%-25s %s", "Errored Tests: ", testResult.errorCount()) + AnsiFormatting.ANSI_RESET);
        if (testResult.failureCount() > 0) System.out.print(AnsiFormatting.ANSI_YELLOW + AnsiFormatting.ANSI_BOLD);
        System.out.println(String.format("%-25s %s", "Failed Tests: ", testResult.failureCount()) + AnsiFormatting.ANSI_RESET);
        System.out.println(AnsiFormatting.ANSI_GRAY + "Total amount of time passed: " + (time) + "ms." + AnsiFormatting.ANSI_RESET);
        System.out.println(AnsiFormatting.ANSI_BOLD + "================== End of Test =======================" + AnsiFormatting.ANSI_RESET);
    }
}
