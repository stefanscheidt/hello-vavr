package hello.vavr.done;


import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.vavr.control.Try.success;
import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

public class TryTest {

    @Test
    public void tryToDivide() {
        Function2<Integer, Integer, Integer> unsafeDivide = (x, y) -> x / y;
        // Function2<Integer, Integer, Try<Integer>> tryDivide = (x, y) -> Try.of(() -> unsafeDivide.apply(x, y));
        var tryDivide = Function2.liftTry(unsafeDivide);

        assertThat(tryDivide.apply(10, 2)).isEqualTo(success(5));

        var failingDivision = tryDivide.apply(10, 0);
        assertThat(failingDivision.isFailure()).isTrue();
        assertThat(failingDivision.getCause()).isInstanceOf(ArithmeticException.class);
        assertThat(failingDivision.getOrElse(MAX_VALUE)).isEqualTo(MAX_VALUE);
        assertThat(failingDivision.recover(ArithmeticException.class, MAX_VALUE).get()).isEqualTo(MAX_VALUE);
    }

    @Test
    public void tryCallService() {
        Function1<String, Try<Response>> tryCall = value -> Try.of(() -> callService(value));

        System.out.println(
                tryCall.apply("INVALID")
                        .flatMap(reponse -> tryCall.apply(reponse.value))
                        .map(reponse -> reponse.value.toUpperCase())
        );


    }

    private Response callService(String value) throws IOException {
        if (value == null) throw new IOException();
        return value.equals("INVALID") ? new Response(null) : new Response(value);
    }

    private static class Response {
        private final String value;

        private Response(String value) {
            this.value = value == null ? null : value + "'";
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
