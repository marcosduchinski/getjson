package pt.iscte.mei.pa.controller

import annotations.Mapping
import annotations.Path

@Mapping("fibo")
class FiboController {

    @Mapping("sequence/{n}")
    fun path(
        @Path n: Int
    ): List<Int> {
        if (n == 0) {
            return listOf(0)
        }
        if (n == 1) {
            return listOf(0, 1)
        }
        val sequence = mutableListOf<Int>(0, 1)
        for (i in 2..n) {
            sequence.add(sequence[i - 1] + sequence[i - 2])
        }
        return sequence
    }
}