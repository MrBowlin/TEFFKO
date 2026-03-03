package framework;

import java.util.ArrayList;

public class TestResult {
    protected ArrayList<String> errorList = new ArrayList<>();
    protected int errors = 0;
    protected int successes = 0;

    public void addError(String message) {
        errorList.add(message);
        errors++;
    }

    public void addSuccess() {
        successes++;
    }

    public int errorCount() {
        return errors;
    }

    public int successCount() {
        return successes;
    }

    public int runCount() {
        return errors + successes;
    }
}
