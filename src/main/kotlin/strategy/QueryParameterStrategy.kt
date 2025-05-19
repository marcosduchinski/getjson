package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import pt.iscte.mei.pa.command.ControllerCommand
import java.net.URI
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class QueryParameterStrategy : DispatchStrategy {

    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean =
        uri.query != null && registry.getByPath(buildPathWithQuery(uri)) != null

    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val queryParams = uri.query.split("&").associate {
            val (key, value) = it.split("=")
            key to value
        }
        val path = buildPathWithQuery(uri)
        val (controller, function) = registry.getByPath(path)!!

        val typedArgs = function.parameters
            .filter { it.kind == KParameter.Kind.VALUE }
            .map {
                val value = queryParams[it.name]!!
                convertToType(value, it.type.classifier as KClass<*>)
            }
        return ControllerCommand(controller, function, typedArgs).execute()
    }

    private fun buildPathWithQuery(uri: URI): String {
        val keys = uri.query.split("&").map { it.split("=")[0] }
        val params = keys.joinToString("&") { "${it}={${it}}" }
        return uri.path + "?" + params
    }
}