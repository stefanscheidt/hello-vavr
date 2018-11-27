package hello.vavr;

import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TryTest {

    @Test
    public void tryToDivide() {
        Function2<Integer, Integer, Integer> unsafeDivide = (x, y) -> x / y;

        Function2<Integer, Integer, Try<Integer>> tryDivide = Function2.liftTry(unsafeDivide);

        Try<Integer> success = tryDivide.apply(10, 5);
        assertThat(success.isSuccess()).isTrue();

        assertThat(success).isEqualTo(Try.success(2));
        Try<Integer> failure = tryDivide.apply(10, 0);
        assertThat(failure.isFailure()).isTrue();
        assertThat(failure.getCause()).isInstanceOf(ArithmeticException.class);
        assertThat(failure.getOrElse(MAX_VALUE)).isEqualTo(MAX_VALUE);
        assertThat(failure.recover(ArithmeticException.class, MAX_VALUE)).isEqualTo(MAX_VALUE);
    }

    @Test
    @Ignore
    public void tryToReadFiles() {
        fail("TBD");
    }

    private List<String> readLinesFrom(String filename) throws Exception {
        var resource = this.getClass().getResource(filename);
        if (resource == null) throw new IOException("Resource not found: " + filename);
        return List.ofAll(Files.readAllLines(Paths.get(resource.toURI()), Charset.defaultCharset()));
    }

}
