package hello.vavr;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    /**
     * List.of(x1, x2, x3, ..., xn).foldLeft(init, f)
     *     = f.apply(f.apply(...(f.apply(f.apply(init, x1), x2), ..., xn))
     */
    @Test
    public void foldLeftTest() {
        Function1<List<Integer>, Integer> sum = xs -> xs.foldLeft(0, (s, x) -> s + x);
        assertThat(sum.apply(List.rangeClosed(1, 10))).isEqualTo(10/2 * (10 + 1));

        Function1<List<Integer>, Integer> prod = xs -> xs.foldLeft(1, (p, x) -> p * x);
        assertThat(prod.apply(List.rangeClosed(1, 10))).isEqualTo(3_628_800);

        Function1<List<Integer>, Integer> count = xs -> xs.foldLeft(0, (n, x) -> n + 1);
        assertThat(count.apply(List.rangeClosed(1, 10))).isEqualTo(10);

        Function1<List<Integer>, List<Integer>> reverse = xs -> xs.foldLeft(List.empty(), List::prepend);
        assertThat(reverse.apply(List.rangeClosed(1, 5))).isEqualTo(List.of(5, 4, 3, 2, 1));

        Function1<List<Boolean>, Boolean> and = bs -> bs.foldLeft(true, (p, b) -> p && b);
        assertThat(and.apply(List.of(true, true, true))).isEqualTo(true);
        assertThat(and.apply(List.of(true, true, false))).isEqualTo(false);

        Function1<List<Boolean>, Boolean> or = bs -> bs.foldLeft(false, (p, b) -> p || b);
        assertThat(or.apply(List.of(true, true, false))).isEqualTo(true);
        assertThat(or.apply(List.of(false, false, false))).isEqualTo(false);
    }

    @Test
    public void qsortTest() {
        var random = new Random();
        var randomNumbers = List.range(0, 10).map(it -> random.nextInt());
        var sortedNumbers = qsort(randomNumbers);
        var pairs = sortedNumbers.zipWith(sortedNumbers.tail(), Tuple::of);
        assertThat(pairs).allMatch(pair -> pair._1 <= pair._2);
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
