package hello.vavr;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

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

    private Tuple2<Integer, Integer> cell(int x, int y) {
        return Tuple.of(x, y);
    }

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
        assertThat(neighboursOf(cell(2, 2)))
                .containsExactlyInAnyOrder(
                        cell(1, 3), cell(2, 3), cell(3, 3),
                        cell(1, 2), cell(3, 2),
                        cell(1, 1), cell(2, 1), cell(3, 1)
        );
    }

    private List<Tuple2<Integer, Integer>> neighboursOf(Tuple2<Integer, Integer> cell) {
        var deltas = List.of(-1, 0, 1);
        return deltas.flatMap(dx -> deltas.map(dy -> cell(dx, dy)))
                .filter(it -> !it.equals(cell(0, 0)))
                .map(it -> cell(cell._1 + it._1, cell._2 + it._2));
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
        assertThat(candidatesFor(List.of(cell(1, 1), cell(2, 2))))
                .containsExactlyInAnyOrder(
                        cell(1, 3), cell(2, 3), cell(3, 3),
                        cell(0, 2), cell(1, 2), cell(2, 2), cell(3, 2),
                        cell(0, 1), cell(1, 1), cell(2, 1), cell(3, 1),
                        cell(0, 0), cell(1, 0), cell(2, 0)
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
        var livingCells = List.of(cell(1, 1), cell(2, 1), cell(2, 2));
        assertThat(numberOfLivingNeighbours(cell(3, 1), livingCells)).isEqualTo(2);
    }

    private int numberOfLivingNeighbours(Tuple2<Integer, Integer> cell, List<Tuple2<Integer, Integer>> livingCells) {
        return neighboursOf(cell).filter(livingCells::contains).size();
    }

    /**
     * <p><pre>
     * 4 . . . . .
     * 3 . . . . .
     * 2 . * * * .
     * 1 . . . . .
     * 0 . . . . .
     *   0 1 2 3 4
     * </pre></p>
     *
     * becomes
     *
     * <p><pre>
     * 4 . . . . .
     * 3 . . * . .
     * 2 . . * . .
     * 1 . . * . .
     * 0 . . . . .
     *   0 1 2 3 4
     * </pre></p>
     */
    @Test
    public void evolveBlinker() {
        var world = List.of(cell(1, 2), cell(2, 2), cell(3, 2));
        assertThat(evolve(world))
                .containsExactlyInAnyOrder(
                        cell(2, 1), cell(2, 2), cell(2, 3)
                );
    }

    private List<Tuple2<Integer, Integer>> evolve(List<Tuple2<Integer, Integer>> world) {
        return candidatesFor(world).filter(it -> willLive(it, world));
    }

    private boolean willLive(Tuple2<Integer, Integer> cell, List<Tuple2<Integer, Integer>> livingCells) {
        var numberOfLivingNeighbours = numberOfLivingNeighbours(cell, livingCells);
        var isAlive = livingCells.contains(cell);
        return isAlive
                ? List.of(2, 3).contains(numberOfLivingNeighbours)
                : List.of(3).contains(numberOfLivingNeighbours);
    }

}
