package hello.vavr;

import io.vavr.Function2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.fail;

public class OptionTest {

    @Test
    public void saveDivideTest() {
        Function2<Integer, Integer, Integer> unsafeDiv = (x, y) -> x / y;

        Function2<Integer, Integer, Option<Integer>> safeDiv = Function2.lift(unsafeDiv);

        Function2<Integer, Integer, Try<Integer>> tryDiv = Function2.liftTry(unsafeDiv);

        System.out.println(Try.of(() -> 1 / 0));

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
