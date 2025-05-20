package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import pt.iscte.mei.pa.command.ControllerCommand
import java.net.URI
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class QueryParameterStrategy : DispatchStrategy {

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
    private fun <T> List<T>.permutation(): List<List<T>> {
        //[n,text]
        if (size <= 1) return listOf(this)
        return flatMapIndexed { index, element ->
            val rest = this - element
            rest.permutation().map { listOf(element) + it }
        }
    }
}