package pt.iscte.mei.pa.http

import java.net.URI
import kotlin.reflect.KFunction

class Dispatcher (val registry: ClassRegistry) {

    private fun isStrictlyEqualPath(endpoint : Pair<Any, KFunction<*>>): Any {
        val controller = endpoint.first
        val function = endpoint.second
        return function.call(controller) as Any
    }

    private fun hasQueryParameter(path : String, query : String): Any {
        val queryParams = query.split("&").associate {
            //will fail to a parameter with multiple value like ?n=1&n=2&n=3
            val (key, value) = it.split("=")
            key to value
        }
        val newPath = path + "?" + queryParams.keys.joinToString(separator = "&") { it + "={${it}}" }
        val endpoint = registry.getByPath(newPath);
        if (endpoint != null) {
            val paramsValues = queryParams.values.map { it.toIntOrNull() ?: it}
            val controller = endpoint.first
            val function = endpoint.second
            return function.call(controller, *paramsValues.toTypedArray()) as Any
        } else {
            return "404"
        }
    }

    private fun hasPathParameter(fullPath : String): Any {
        val pathParameterValue = fullPath.split("/").last()
        val path = fullPath.split("/").dropLast(1).joinToString( separator = "/")
        val newPath = path + "/{pathvar}"
        return registry.getByPath(newPath).let { pair ->
            (if (pair != null) {
                val controller = pair.first
                val function = pair.second
                function.call(controller, pathParameterValue)
            } else {
                "404"
            }) as Any
        }

    }

    fun execute(uri: URI): Any {

        val endpoint = registry.getByPath(uri.path);

        if (endpoint != null) {
            return isStrictlyEqualPath(endpoint)
        }
        if(uri.query != null) {
            return hasQueryParameter(uri.path, uri.query)
        }

        return hasPathParameter(uri.path)
    }
}