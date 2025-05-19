package pt.iscte.mei.pa.controller

import annotations.Mapping
import annotations.Path

@Mapping("fibo")
class FiboController {

    @Mapping("sequence/{pathvar}")
    fun path(
        @Path pathvar: Int
    ): List<Int> {
        if (pathvar == 0) {
            return listOf(0)
        }
        if (pathvar == 1) {
            return listOf(0, 1)
        }
        val sequence = mutableListOf<Int>(0, 1)
        for (i in 2..pathvar) {
            sequence.add(sequence[i - 1] + sequence[i - 2])
        }
        return sequence
    }
}