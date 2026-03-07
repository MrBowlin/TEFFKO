package tests;

import java.lang.reflect.Method;

public interface TestFixture {
    void setUp(Method method);

    void tearDown(Method method);
}
