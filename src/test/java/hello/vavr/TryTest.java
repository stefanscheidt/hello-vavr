package hello.vavr;

import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class TryTest {

    @Test
    public void tryToDivide() {
        Function2<Integer, Integer, Integer> unsaveDivide = (x, y) -> x / y;
        Function2<Integer, Integer, Try<Integer>> tryDivide = (x, y) -> Try.of(() -> unsaveDivide.apply(x, y));

        assertThat(tryDivide.apply(10, 2)).isEqualTo(Try.success(5));

        var failingDivision = tryDivide.apply(10, 0);
        assertThat(failingDivision.isFailure()).isTrue();

        failingDivision.onFailure(ex -> assertThat(ex).isInstanceOf(ArithmeticException.class));

        var recovered = failingDivision.recover(ArithmeticException.class, Integer.MAX_VALUE).get();
        assertThat(recovered).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void tryToReadFiles() {
        String result = Try.of(() -> linesFromFile("/first.txt").get(0))
                .flatMapTry(filename -> Try.of(() -> linesFromFile(filename).get(0)))
                .flatMapTry(filename -> Try.of(() -> linesFromFile(filename).get(0)))
                .getOrElse("FAILED");
        assertThat(result).isEqualTo("SUCCESS");
    }

    private List<String> linesFromFile(String filename) throws URISyntaxException, IOException {
        var path = Paths.get(this.getClass().getResource(filename).toURI());
        return List.ofAll(Files.readAllLines(path, Charset.defaultCharset()));
    }

}
