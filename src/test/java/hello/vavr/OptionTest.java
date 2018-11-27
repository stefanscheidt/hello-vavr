package hello.vavr;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class OptionTest {

    @Test
    public void saveDivideTest() {
        Function2<Integer, Integer, Integer> unsafeDivide = (x, y) -> x / y;
        assertThatExceptionOfType(ArithmeticException.class).isThrownBy(() ->
            unsafeDivide.apply(1, 0)
        );

        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(unsafeDivide);

        assertThat(safeDivide.apply(1, 0)).isEqualTo(Option.none());

        Function1<Integer, Option<Integer>> reciprocalPercent = safeDivide.apply(100);

        var percentages = List.of(-2, -1, 0, 1, 2).map(reciprocalPercent);

        assertThat(percentages).isEqualTo(List.of(
                Option.some(-50), Option.some(-100), Option.none(), Option.some(100), Option.some(50)
        ));

        assertThat(percentages.flatMap(Function1.identity())).isEqualTo(List.of(
                -50, -100, 100, 50
        ));
    }


    private Integer divideOrNull(int x, int y) {
        return y == 0 ? null : x / y;
    }

    @Test
    public void optionalIsNotMonadic() {
        assertThat(Optional.of(2).map(y -> divideOrNull(10, y))).isEqualTo(Optional.of(5));
        assertThat(Optional.of(0).map(y -> divideOrNull(10, y))).isEqualTo(Optional.empty());
    }

    @Test
    public void optionIsMonadic() {
        assertThat(Option.some(2).map(y -> divideOrNull(10, y))).isEqualTo(Option.some(5));
        assertThat(Option.some(0).map(y -> divideOrNull(10, y))).isEqualTo(Option.some(null));

        var optionResult = Option.some(0).flatMap(y -> Option.of(divideOrNull(10, y)));
        assertThat(optionResult).isEqualTo(Option.none());
    }

}
