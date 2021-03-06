package hello.vavr;

import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import static io.vavr.Function0.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class FizzBuzzTest {

    @Test
    public void fizzBuzzTest() {
        assertThat(fizzBuzz(20)).isEqualTo(List.of(
                "1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz",
                "11", "Fizz", "13", "14", "FizzBuzz", "16", "17", "Fizz", "19", "Buzz"
        ));
    }

    private List<String> fizzBuzz(int count) {
        var fizz = Stream.fill(2, constant("")).append("Fizz").cycle(); // Stream.of("", "", "Fizz").cycle();
        var buzz = Stream.fill(4, constant("")).append("Buzz").cycle(); // Stream.of("", "", "", "", "Buzz").cycle();
        var fizzBuzz = fizz.zipWith(buzz, (fst, snd) -> fst + snd);
        return fizzBuzz
                .zipWithIndex((fb, i) -> fb.isBlank() ? "" + (i + 1) : fb)
                .take(count)
                .toList();
    }

}
