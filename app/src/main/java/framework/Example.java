package framework;

public class Example extends TestCase{

    @UnitTest
    public void testfun() {
        assertTrue(1 == 2, "message");
    }

    @UnitTest
    public void testfun2() {
        assertTrue(1==3, "message2");
    }
}
