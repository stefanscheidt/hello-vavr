package hello.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Rules:
 * <ol>
 *     <li>Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.</li>
 *     <li>Any live cell with more than three live neighbours dies, as if by overcrowding.</li>
 *     <li>Any live cell with two or three live neighbours lives on to the next generation.</li>
 *     <li>Any dead cell with exactly three live neighbours becomes a live cell.</li>
 * </ol>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life">Conway's Game of Life</a>
 * @see <a href="http://codingdojo.org/kata/GameOfLif":>Game of Life Kata</a>
 */
public class GameOfLifeTest {

    /**
     * <pre>
     * 4 . . . . .
     * 3 . o o o .
     * 2 . o * o .
     * 1 . o o o .
     * 0 . . . . .
     *   0 1 2 3 4
     * </pre>
     */
    @Test
    public void neighboursTest() {
        assertThat(neighboursOf(Tuple.of(2, 2))).containsExactlyInAnyOrder(
                Tuple.of(1, 3), Tuple.of(2, 3), Tuple.of(3, 3),
                Tuple.of(1, 2), Tuple.of(3, 2),
                Tuple.of(1, 1), Tuple.of(2, 1), Tuple.of(3, 1)
        );
    }

    private List<Tuple2<Integer, Integer>> neighboursOf(Tuple2<Integer, Integer> cell) {
        var deltas = List.of(-1, 0, 1);
        return deltas.flatMap(dx -> deltas.map(dy -> Tuple.of(dx, dy)))
                .map(delta -> Tuple.of(cell._1 + delta._1, cell._2 + delta._2))
                .filter(it -> !it.equals(cell));
    }

    /**
     * <pre>
     * 4 . . . . .
     * 3 . o o o .
     * 2 o o * o .
     * 1 o * o o .
     * 0 o o o . .
     *   0 1 2 3 4
     * </pre>
     */
    @Test
    public void candidatesTest() {
        assertThat(candidatesFor(List.of(Tuple.of(1, 1), Tuple.of(2, 2)))).containsExactlyInAnyOrder(
                Tuple.of(1, 3), Tuple.of(2, 3), Tuple.of(3, 3),
                Tuple.of(0, 2), Tuple.of(1, 2), Tuple.of(2, 2), Tuple.of(3, 2),
                Tuple.of(0, 1), Tuple.of(1, 1), Tuple.of(2, 1), Tuple.of(3, 1),
                Tuple.of(0, 0), Tuple.of(1, 0), Tuple.of(2, 0)
        );
    }

    private List<Tuple2<Integer, Integer>> candidatesFor(List<Tuple2<Integer, Integer>> cells) {
        return cells.flatMap(this::neighboursOf).prependAll(cells).distinct();
    }

    /**
     * <pre>
     * 4 . . . . .
     * 3 . . . . .
     * 2 . . * . .
     * 1 . * * o .
     * 0 . . . . .
     *   0 1 2 3 4
     * </pre>
     */
    @Test
    public void numberOfLivingNeighboursTest() {
        var livingCells = List.of(Tuple.of(1, 1), Tuple.of(2, 1), Tuple.of(2, 2));
        assertThat(numberOfLivingNeighbours(Tuple.of(3, 1), livingCells)).isEqualTo(2);
    }

    private int numberOfLivingNeighbours(Tuple2<Integer, Integer> cell, List<Tuple2<Integer, Integer>> livingCells) {
        return neighboursOf(cell).filter(livingCells::contains).size();
    }

}
