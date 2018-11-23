package hello.vavr

import io.vavr.Tuple


fun main(args: Array<String>) {
    val java8 = Tuple.of("Java", 8)
    val vavr1 = java8.apply { s, v -> Tuple.of(s.substring(2) + "vr", v / 8) }
    println(vavr1)
}