package hello.vavr;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

public class TryTest {

    @Test
    public void tryToDivide() {
        Function2<Integer, Integer, Integer> unsafeDivide = (x, y) -> x / y;
        // Function2<Integer, Integer, Try<Integer>> tryDivide = (x, y) -> Try.of(() -> unsafeDivide.apply(x, y));
        var tryDivide = Function2.liftTry(unsafeDivide);

        assertThat(tryDivide.apply(10, 2)).isEqualTo(Try.success(5));

        var failingDivision = tryDivide.apply(10, 0);
        assertThat(failingDivision.isFailure()).isTrue();
        assertThat(failingDivision.getCause()).isInstanceOf(ArithmeticException.class);
        assertThat(failingDivision.getOrElse(MAX_VALUE)).isEqualTo(MAX_VALUE);
        assertThat(failingDivision.recover(ArithmeticException.class, MAX_VALUE).get()).isEqualTo(MAX_VALUE);
    }

    @Test
    public void tryToReadFiles() {
        Function1<String, Try<List<String>>> tryReadLines = filename -> Try.of(() -> readLinesFrom(filename));

        var result = tryReadLines.apply("/first.txt")
                .mapTry(lines -> lines.get(1))
                .flatMapTry(tryReadLines::apply)
                .mapTry(lines -> lines.get(1))
                .onFailure(Throwable::printStackTrace);

        assertThat(result.getOrElse("FAILED")).isEqualTo("SUCCESS");
    }

    private List<String> readLinesFrom(String filename) throws Exception {
        var resource = this.getClass().getResource(filename);
        if (resource == null) throw new IOException("Resource not found: " + filename);
        return List.ofAll(Files.readAllLines(Paths.get(resource.toURI()), Charset.defaultCharset()));
    }

}
