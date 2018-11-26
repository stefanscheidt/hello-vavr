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

        Function2<Integer, Integer, Option<Integer>> saveDivide = Function2.lift(unsafeDivide);

        assertThat(saveDivide.apply(1, 0)).isEqualTo(Option.none());

        Function1<Integer, Option<Integer>> reciprocalPercent = saveDivide.apply(100);

        List<Option<Integer>> percentages = List.of(-2, -1, 0, 1, 2).map(reciprocalPercent);

        assertThat(percentages).isEqualTo(
                List.of(Option.some(-50), Option.some(-100), Option.none(), Option.some(100), Option.some(50))
        );

        assertThat(percentages.flatMap(Function1.identity())).isEqualTo(
                List.of(-50, -100, 100, 50)
        );
    }

    private Integer divideOrNull(int x, int y) {
        return y == 0 ? null : x / y;
    }

    @Test
    public void optionalIsNotMonadic() {
        assertThat(Optional.of(2).map(y -> divideOrNull(10, y))).isEqualTo(Optional.of(5));
        assertThat(Optional.of(0).map(y -> divideOrNull(10, y))).isEqualTo(Optional.empty());

        var optionalResult = Optional.of(0).map(y -> divideOrNull(10, y));
        assertThat(optionalResult.map(it -> it * 10)).isEqualTo(Optional.empty());
    }

    @Test
    public void optionIsMonadic() {
        assertThat(Option.some(2).map(y -> divideOrNull(10, y))).isEqualTo(Option.some(5));
        assertThat(Option.some(0).map(y -> divideOrNull(10, y))).isEqualTo(Option.some(null));

        var optionResult = Option.some(0).map(y1 -> divideOrNull(10, y1));
        assertThatNullPointerException().isThrownBy(() ->
                optionResult.map(it -> it * 10)
        );

        var optionResult2 = Option.some(0)
                .map(y1 -> divideOrNull(10, y1))
                .flatMap(result -> Option.of(result).map(it -> it * 10));
        assertThat(optionResult2).isEqualTo(Option.none());

        var optionResult3 = Option.some(0)
                .flatMap(y -> Option.of(divideOrNull(10, y)));
        assertThat(optionResult3).isEqualTo(Option.none());
    }

}
