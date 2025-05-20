package pt.iscte.mei.pa

import KsonLib
import annotations.Mapping
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import pt.iscte.mei.pa.Dispatcher
import pt.iscte.mei.pa.EndpointRegistry
import java.net.InetSocketAddress
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Main class to start the server and register endpoints.
 *
 * @param clazzs Classes to be registered as endpoints.
 */
class GetJson(vararg clazzs: KClass<*>) {

    val contexts = mutableSetOf<String>()


    init {
        for (clazz in clazzs) {
            EndpointRegistry.register(clazz)
            extractContext(clazz)
        }
    }

    private fun extractContext(clazz: KClass<*>) {
        val context = clazz.findAnnotation<Mapping>()?.name
        if (context != null) {
            contexts += context
        } else {
            throw IllegalArgumentException("Class ${clazz.simpleName} does not have a @Mapping annotation")
        }
    }

    /**
     * Starts the server on the specified port.
     * This method creates an HTTP server and registers the contexts for each class.
     * It also sets up a handler to process incoming requests.
     *
     * @param port The port to start the server on. Default is 8080.
     */
    fun start(port: Int = 8080) {
        //https://gist.github.com/trevorwhitney/23f7d8ee9e2f92d629e149a7fde01f21
        val server = HttpServer.create(InetSocketAddress(port), 0)
        contexts.forEach { server.createContext("/" + it, MyHandler()) }
        server.executor = null // creates a default executor
        server.start()
        println("Server started on port $port")
    }

    /**
     * Handler for processing incoming HTTP requests.
     * It uses the Dispatcher to handle the request and returns the response as JSON.
     */
    class MyHandler : HttpHandler {
        private val dispatcher = Dispatcher(EndpointRegistry)

        /**
         * Handles the incoming HTTP exchange.
         * It retrieves the request URI, processes it using the Dispatcher, and sends the response.
         * The JSON response is generated using KsonLib, a library for JSON serialization.
         *
         * @param exchange The HTTP exchange containing the request and response.
         */
        override fun handle(exchange: HttpExchange) {
            val response = KsonLib(dispatcher.execute(exchange.requestURI)).asJson();
            if ("404" in response) {
                exchange.sendResponseHeaders(404, -1)
                return
            }
            exchange.responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length.toLong())
            exchange.responseBody.use { os ->
                os.write(response.toByteArray())
            }
        }
    }
}