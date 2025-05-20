package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import pt.iscte.mei.pa.command.ControllerCommand
import java.net.URI
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/**
 * Strategy for handling requests with query parameters in the URI.
 *
 * This strategy checks if the URI has query parameters and if the path matches a registered endpoint
 * with a query parameter. If both conditions are met, it executes the corresponding controller method
 * with the extracted query parameter values.
 *
 * @constructor Creates an instance of QueryParameterStrategy.
 */
class QueryParameterStrategy : DispatchStrategy {

    /**
     * Determines if the strategy can handle the given URI.
     *
     * @param uri The URI to check.
     * @param registry The endpoint registry to check against.
     * @return True if the strategy can handle the URI, false otherwise.
     */
    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean {
        if (uri.query == null) {
            return false
        }
        val paths = buildPathsWithQueryPermutations(uri)
        paths.forEach {
            if (registry.getByPath(it) != null) {
                return true
            }
        }
        return false
    }

    /**
     * Handles the request for the given URI and registry.
     *
     * @param uri The URI to handle.
     * @param registry The endpoint registry to handle against.
     * @return The result of handling the request.
     */
    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val queryParams = uri.query.split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }
        val paths = buildPathsWithQueryPermutations(uri)
        var controllerFun: Pair<Any, KFunction<*>>?? = null
        for (path in paths) {
            controllerFun = registry.getByPath(path)
            if (controllerFun != null) {
                break
            }
        }
        val controller = controllerFun!!.first
        val function = controllerFun.second
        val typedArgs = function.parameters
            .filter { it.kind == KParameter.Kind.VALUE }
            .map {
                val value = queryParams[it.name]!!
                convertToType(value, it.type.classifier as KClass<*>)
            }
        return ControllerCommand(controller, function, typedArgs).execute()
    }

    /**
     * Builds a list of paths with query parameter permutations based on the given URI.
     *
     * @param uri The URI to build paths from.
     * @return A list of paths with query parameter permutations.
     */
    private fun buildPathsWithQueryPermutations(uri: URI): List<String> {
        val keys = uri.query.split("&")
            .map { it.substringBefore("=") }
        return keys.permutation().map { perm ->
            val paramString = perm.joinToString("&") { "$it={${it}}" }
            "${uri.path}?$paramString"
        }
    }

    // Extension function to generate list of lists of permutations
    // O(n!)
    // [n,text,d] => [[n,text,d],[n,d,text],[text,n,d],[text,d,n],[d,n,text],[d,text,n]]
    private fun <T> List<T>.permutation(): List<List<T>> {
        //[n,text]
        if (size <= 1) return listOf(this)
        return flatMapIndexed { index, element ->
            val rest = this - element
            rest.permutation().map { listOf(element) + it }
        }
    }
}