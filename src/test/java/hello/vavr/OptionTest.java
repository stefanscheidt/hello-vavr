package hello.vavr;

import org.junit.Test;

import static org.assertj.core.api.Assertions.fail;

public class OptionTest {

    @Test
    public void saveDivideTest() {
        fail("TBD");
    }

    private Integer divideOrNull(int x, int y) {
        return y == 0 ? null : x / y;
    }

    @Test
    public void optionalIsNotMonadic() {
        fail("TBD");
    }

    @Test
    public void optionIsMonadic() {
        fail("TBD");
    }

}
