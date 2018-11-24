# FizzBuzz

```
private List<String> fizzBuzz(int count) {

    var fizz = Stream.of("", "", "Fizz").cycle();
    // var fizz = List.fill(2, constant("")).append("Fizz").toStream().cycle();

    var buzz = Stream.of("", "", "", "", "Buzz").cycle();
    // var buzz = List.fill(4, constant("")).append("Buzz").toStream().cycle();

    var fizzBuzz = fizz.zipWith(buzz, (fst, snd) -> fst + snd);
    return fizzBuzz
            .zipWithIndex((fb, i) -> fb.isBlank() ? "" + (i + 1) : fb)
            .take(count)
            .toList();

}
```

# Game of Life

```
private List<Tuple2<Integer, Integer>> neighboursOf(Tuple2<Integer, Integer> cell) {
    var deltas = List.of(-1, 0, 1);
    return deltas.flatMap(x -> deltas.map(y -> Tuple.of(x, y)))
            .map(delta -> Tuple.of(cell._1 + delta._1, cell._2 + delta._2))
            .filter(it -> !it.equals(cell));
}
```

```
private List<Tuple2<Integer, Integer>> candidatesFor(List<Tuple2<Integer, Integer>> cells) {
    return cells.flatMap(this::neighboursOf).prependAll(cells).distinct();
}
```

```
private int numberOfLivingNeighbours(Tuple2<Integer, Integer> cell, List<Tuple2<Integer, Integer>> livingCells) {
    return neighboursOf(cell).filter(livingCells::contains).size();
}
```

# Option

```
@Test
public void saveDivideTest() {
    Function2<Integer, Integer, Integer> unsafeDivide = (x, y) -> x / y;

    assertThatExceptionOfType(ArithmeticException.class).isThrownBy(() ->
            unsafeDivide.apply(1, 0)
    );
    
    // Function2<Integer, Integer, Option<Integer>>
    var saveDivide = Function2.lift(unsafeDivide);

    assertThat(saveDivide.apply(1, 0)).isEqualTo(Option.none());

    // Function1<Integer, Option<Integer>>
    var reciprocalPercent = saveDivide.apply(100);

    // List<Option<Integer>>
    var percentages = List.of(-2, -1, 0, 1, 2).map(reciprocalPercent);

    assertThat(percentages).containsExactly(
            Option.some(-50), Option.some(-100), Option.none(), Option.some(100), Option.some(50));

    assertThat(percentages.flatMap(Function1.identity())).containsExactly(-50, -100, 100, 50);
}
```

```
@Test
public void optionalIsNotMonadic() {
    assertThat(Optional.of(2).map(y -> divideOrNull(10, y))).isEqualTo(Optional.of(5));
    assertThat(Optional.of(0).map(y -> divideOrNull(10, y))).isEqualTo(Optional.empty());

    var optionalResult = Optional.of(0).map(y -> divideOrNull(10, y));
    assertThat(optionalResult.map(it -> it * 10)).isEqualTo(Optional.empty());
}
```

```
@Test
public void optionIsMonadic() {
    assertThat(Option.some(2).map(y -> divideOrNull(10, y))).isEqualTo(Option.some(5));
    assertThat(Option.some(0).map(y -> divideOrNull(10, y))).isEqualTo(Option.some(null));

    var optionResult = Option.some(0).map(y1 -> divideOrNull(10, y1));
    assertThatNullPointerException().isThrownBy(() ->
            optionResult.map(it -> it * 10)
    );

    var optionResult2 = Option.some(0)
            .map(y1 -> divideOrNull(10, y1))
            .flatMap(result -> Option.of(result).map(it -> it * 10));
    assertThat(optionResult2).isEqualTo(Option.none());

    var optionResult3 = Option.some(0)
            .flatMap(y -> Option.of(divideOrNull(10, y)));
    assertThat(optionResult3).isEqualTo(Option.none());
}
```

# Try

```
@Test
public void tryToDivide() {
    Function2<Integer, Integer, Integer> unsafeDivide = (x, y) -> x / y;
    // Function2<Integer, Integer, Try<Integer>> tryDivide = (x, y) -> Try.of(() -> unsafeDivide.apply(x, y));
    var tryDivide = Function2.liftTry(unsafeDivide);

    assertThat(tryDivide.apply(10, 2)).isEqualTo(Try.success(5));

    var failingDivision = tryDivide.apply(10, 0);
    assertThat(failingDivision.isFailure()).isTrue();
    assertThat(failingDivision.getCause()).isInstanceOf(ArithmeticException.class);
    assertThat(failingDivision.getOrElse(MAX_VALUE)).isEqualTo(MAX_VALUE);
    assertThat(failingDivision.recover(ArithmeticException.class, MAX_VALUE).get()).isEqualTo(MAX_VALUE);
}
```

```
@Test
public void tryToReadFiles() {
    Function1<String, Try<List<String>>> tryReadLinesFrom = filename -> Try.of(() -> readLinesFrom(filename));

    var result = tryReadLinesFrom.apply("/first.txt")
            .mapTry(lines -> lines.get(1))
            .flatMapTry(tryReadLinesFrom::apply)
            .mapTry(lines -> lines.get(1))
            .onFailure(Throwable::printStackTrace);

    assertThat(result.getOrElse("FAILED")).isEqualTo("SUCCESS");
}
```
