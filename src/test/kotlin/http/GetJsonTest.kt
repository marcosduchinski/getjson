package http

import KsonLib
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pt.iscte.mei.pa.GetJson
import pt.iscte.mei.pa.controller.Controller
import java.net.HttpURLConnection
import java.net.URI

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetJsonTest {

    val port = 8080
    val url = "http://localhost"

    @BeforeAll
    fun init() {
        val app = GetJson(Controller::class)
        app.start(port)
    }

    @AfterAll
    fun tearDown() {
        //app.stop()
    }

    @Test
    fun should_return_int_list_as_json() {
        val connection = URI("$url:$port/api/ints")
            .toURL()
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().readText()
        assertEquals(200, responseCode)
        assertEquals(KsonLib(listOf(1, 2, 3)).asJson(), responseBody)
    }

    @Test
    fun should_return_pair_of_string_as_json() {
        val connection = URI("$url:$port/api/pair")
            .toURL()
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().readText()
        assertEquals(200, responseCode)
        assertEquals(KsonLib(mapOf("first" to "um", "second" to "dois")).asJson(), responseBody)
    }

    @Test
    fun should_return_path_params_a_as_json() {
        val connection = URI("$url:$port/api/path/a")
            .toURL()
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().readText()
        assertEquals(200, responseCode)
        assertEquals(KsonLib("a!").asJson(), responseBody)
    }

    @Test
    fun should_return_path_params_b_as_json() {
        val connection = URI("$url:$port/api/path/b")
            .toURL()
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().readText()
        assertEquals(200, responseCode)
        assertEquals(KsonLib("b!").asJson(), responseBody)
    }

    @Test
    fun should_return_map_as_json() {
        val connection = URI("$url:$port/api/args?n=3&text=PA")
            .toURL()
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        val responseCode = connection.responseCode
        val responseBody = connection.inputStream.bufferedReader().readText()
        assertEquals(200, responseCode)
        assertEquals(KsonLib(mapOf("PA" to "PAPAPA")).asJson(), responseBody)
    }

}