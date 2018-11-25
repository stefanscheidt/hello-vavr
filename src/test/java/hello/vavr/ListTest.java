package hello.vavr;

import io.vavr.collection.List;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.fail;

public class ListTest {

    /**
     * List.of(x1, x2, x3, ..., xn).foldLeft(init, f) = f.apply(init, x1).apply(x2).apply(x3)...apply(xn)
     */
    @Test
    public void foldLeftTest() {
        fail("TBD");
    }

    @Test
    public void qsortTest() {
        var random = new Random();
        var randomNumbers = List.range(0, 10).map(it -> random.nextInt());
        var sortedNumbers = qsort(randomNumbers);

        fail("assert sortedNumbers are sorted");
    }

    private List<Integer> qsort(List<Integer> xs) {
        return List.empty();
    }

}
