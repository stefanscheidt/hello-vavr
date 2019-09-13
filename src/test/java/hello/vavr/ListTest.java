package hello.vavr;

import io.vavr.Tuple;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ListTest {

    /**
     * List.of(x1, x2, x3, ..., xn).foldLeft(init, f)
     *     = f.apply(f.apply(...(f.apply(f.apply(init, x1), x2), ..., xn))
     */
    @Test
    public void foldLeftTest() {
        fail("TBD");
    }

    @Test
    public void qsortTest() {
        var random = new Random();
        var randomNumbers = List.range(0, 100_000_000).map(it -> random.nextInt());
        var sortedNumbers = qsort(randomNumbers);
        var pair = sortedNumbers.zipWith(sortedNumbers.tail(), Tuple::of);
        assertThat(pair).allMatch(it -> it._1 <= it._2);
    }

    private List<Integer> qsort(List<Integer> xs) {
        if (xs.isEmpty()) {
            return List.empty();
        }
        var head = xs.head();
        var tail = xs.tail();
        var smaller = tail.filter(it -> it <= head);
        var larger = tail.filter(it -> it > head);
        return qsort(larger).prepend(head).prependAll(qsort(smaller));
    }

}
