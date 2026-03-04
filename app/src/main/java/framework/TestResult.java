package framework;

import java.util.ArrayList;

public class TestResult {
    protected ArrayList<String> errorList = new ArrayList<>();
    protected ArrayList<String> failureList = new ArrayList<>();
    protected int errors = 0;
    protected int successes = 0;
    protected int failures = 0;

    protected void addError(String message, String method) {
        errorList.add("At function '" + method + "': " + message);
        errors++;
    }

    protected void addFailure(String message, String method) {
        errorList.add("At function '" + method + "': " + message);
        failures++;
    }

    protected void addSuccess() {
        successes++;
    }

    protected int errorCount() {
        return errors;
    }

    protected int failureCount() {
        return failures;
    }

    protected int successCount() {
        return successes;
    }

    protected int runCount() {
        return errors + successes;
    }
}
