import ru.sharipov.TestRunner;
import ru.sharipov.annotation.After;
import ru.sharipov.annotation.Before;
import ru.sharipov.annotation.Test;

public class TestableClass {

    @Before
    public void setUp1() {
        System.out.println("Before");
    }

    @Test
    public void test() {
        System.out.println("First Test");
    }

    @Test
    public void testThrow() {
        throw new RuntimeException("I thrown exception");
    }

    @After
    public void after() {
        System.out.println("After");
    }

    public static void main(String[] args) {
        TestRunner runner = new TestRunner(TestableClass.class);
        runner.run();
    }
}
