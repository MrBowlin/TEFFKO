package framework;

public class Example extends TestCase{

    @UnitTest
    public void testfun() {
        BusConnection connection = new BusConnection("169.254.30.15");

        connection.writeBoolean(true, "0/0/3");
        connection.writeBoolean(true, "0/0/4");
        boolean value = connection.readBoolean("0/0/5");
        assertTrue(value);

        connection.writeBoolean(false, "0/0/3");
        value = connection.readBoolean("0/0/5");
        assertFalse(value);

        connection.writeBoolean(false, "0/0/4");
        value = connection.readBoolean("0/0/5");
        assertFalse(value);

        connection.writeBoolean(true, "0/0/3");
        value = connection.readBoolean("0/0/5");
        assertFalse(value);

        connection.writeBoolean(true, "0/0/4");
        value = connection.readBoolean("0/0/5");
        assertTrue(value);
    }
}
