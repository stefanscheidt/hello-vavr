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

        Try<Response> result1 = tryCall.apply(null);
        assertThat(result1.isFailure()).isTrue();
        System.out.println("Result 1: " + result1);

        Try<Response> result2 = tryCall.apply("INVALID");
        assertThat(result2.isSuccess()).isTrue();
        System.out.println("Result 2: " + result2.get());

        Try<Response> result3 = result2.flatMap(response -> tryCall.apply(response.value));
        assertThat(result3.isFailure()).isTrue();
        System.out.println("Result 3: " + result3);

        Try<Response> result4 = tryCall.apply("valid");
        assertThat(result4.isSuccess()).isTrue();
        System.out.println("Result 4: " + result4.get());

        Try<String> mappedResult4 = result4.map(response -> response.value.substring(0, 1));
        assertThat(mappedResult4).isEqualTo(success("v"));

        Try<Response> result5 = result4.flatMap(response -> tryCall.apply(response.value));
        assertThat(result5.isSuccess()).isTrue();
        System.out.println("Result 5: " + result5.get());
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
