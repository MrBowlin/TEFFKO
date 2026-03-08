package framework;

import java.util.ArrayList;

public class TestSuite implements Test {
    ArrayList<Test> testList = new ArrayList<>();

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
