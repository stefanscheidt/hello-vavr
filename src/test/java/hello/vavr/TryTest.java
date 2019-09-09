package hello.vavr;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.fail;

public class TryTest {

    @Test
    public void tryToDivide() {
        fail("TBD");
    }

    @Test
    public void tryCallService() {
        fail("TBD");
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
