package pt.iscte.mei.pa.controller

import annotations.Mapping
import annotations.Path

/**
 * This is a sample controller class that demonstrates how to use the GETJSON framework.
 * It contains several methods that are mapped to different endpoints.
 *
 */
@Mapping("fibo")
class FiboController {

    /**
     * Returns the Fibonacci sequence up to the nth number.
     *
     * @param n The number of terms in the Fibonacci sequence to return.
     * @return A list of integers representing the Fibonacci sequence.
     */
    @Mapping("sequence/{n}")
    fun sequence(
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