package pt.iscte.mei.pa.controller

import annotations.Mapping
import annotations.Param
import annotations.Path

@Mapping("fibo")
class FiboController {

    @Mapping("sequence/{n}")
    fun path(
        @Path n: Int
    ): String {
        var a = 0
        var b = 1
        var c: Int
        var result = "0"
        for (i in 1 until n) {
            c = a + b
            a = b
            b = c
            result += ", $c"
        }
        return result
    }
}