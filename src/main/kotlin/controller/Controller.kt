package pt.iscte.mei.pa.controller

import annotations.Mapping
import annotations.Param
import annotations.Path

/**
 * This is a sample controller class that demonstrates how to use the GETJSON framework.
 * It contains several methods that are mapped to different endpoints.
 *
 */
@Mapping("api")
class Controller {

    @Mapping("welcome")
    fun welcome(): String = "Welcome to GETJSON!"

    @Mapping("ints")
    fun demo(): List<Int> = listOf(1, 2, 3)

    @Mapping("emptyList")
    fun empList(): List<String> = emptyList()

    @Mapping("emptyMap")
    fun empMap(): Map<String,String> = emptyMap<String,String>()

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