package hello.vavr;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.Test;

import java.util.Optional;

import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class OptionTest {

    @Test
    public void saveDivideTest() {
        Function2<Integer, Integer, Integer> unsafeDiv = (x, y) -> x / y;
        Function2<Integer, Integer, Option<Integer>> safeDiv = Function2.lift(unsafeDiv);
        Function1<Integer, Option<Integer>> reciprocalPercent = safeDiv.apply(100);

        var percentages = List.of(-2, -1, 0, 1, 2).map(reciprocalPercent);

        assertThat(percentages.flatMap(identity())).isEqualTo(List.of(-50, -100, 100, 50));
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
