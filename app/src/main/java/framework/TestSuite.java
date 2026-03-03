package framework;

import java.util.ArrayList;

public abstract class TestSuite implements Test {
    ArrayList<Test> testList;

    @Override
    public void run(TestResult result) {
        for (Test test : testList) {
            test.run(result);
        }
    }

    public void addTest(Test test) {
        testList.add(test);
    }
}
