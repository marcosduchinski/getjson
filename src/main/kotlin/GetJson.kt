package pt.iscte.mei.pa

import KsonLib
import annotations.Mapping
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import pt.iscte.mei.pa.http.ClassRegistry
import pt.iscte.mei.pa.http.Dispatcher
import java.net.InetSocketAddress
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

class GetJson(vararg clazzs: KClass<*>) {

    val contexts = mutableSetOf<String>()

    init {
        for (clazz in clazzs) {
            ClassRegistry.register(clazz)
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

    fun start(port: Int = 8080) {
        //https://gist.github.com/trevorwhitney/23f7d8ee9e2f92d629e149a7fde01f21
        val server = HttpServer.create(InetSocketAddress(port), 0)
        contexts.forEach { server.createContext("/" + it, MyHandler()) }
        server.executor = null // creates a default executor
        server.start()
        println("Server started on port $port")
    }

    class MyHandler : HttpHandler {
        override fun handle(t: HttpExchange) {

            //here ksonlib is used to convert the response to json
            val response = KsonLib(Dispatcher(ClassRegistry).execute(t.requestURI)).asJson()

            t.sendResponseHeaders(200, response.length.toLong())
            val os = t.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }
}