package hello.vavr;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.fail;

public class TryTest {

    @Test
    public void tryToDivide() {
        fail("TBD");
    }

    @Test
    public void tryToReadFiles() {
        fail("TBD");
    }

    private List<String> readLinesFrom(String filename) throws Exception {
        var resource = this.getClass().getResource(filename);
        if (resource == null) throw new IOException("Resource not found: " + filename);
        return List.ofAll(Files.readAllLines(Paths.get(resource.toURI()), Charset.defaultCharset()));
    }

}
