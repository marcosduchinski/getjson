package pt.iscte.mei.pa.controller

import annotations.Mapping
import annotations.Param
import annotations.Path

@Mapping("api")
class Controller {

    @Mapping("welcome")
    fun welcome(): String = "Welcome to GETJSON!"

    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    @Mapping("pair")
    fun obj(): Pair<String, String> = Pair("um", "dois")

    @Mapping("path/{pathvar}")
    fun path(
        @Path pathvar: String
    ): String = pathvar + "!"

    @Mapping("args")
    fun args(
        @Param n: Int,
        @Param text: String
    ): Map<String, String> = mapOf(text to text.repeat(n))
}