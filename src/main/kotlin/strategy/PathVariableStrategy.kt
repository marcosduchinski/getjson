package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import pt.iscte.mei.pa.command.ControllerCommand
import java.net.URI
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class PathVariableStrategy : DispatchStrategy {

    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean {
        val parts = uri.path.split("/")
        if (uri.query != null || parts.size < 4) return false
        val genericPath = parts.dropLast(1).joinToString("/") + "/{pathvar}"
        return registry.getByPath(genericPath) != null
    }

    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val parts = uri.path.split("/")
        val value = parts.last()
        val path = parts.dropLast(1).joinToString("/") + "/{pathvar}"
        val (controller, function) = registry.getByPath(path)!!
        val typedArg = function.parameters
            .filter { it.kind == KParameter.Kind.VALUE }
            .map {
                convertToType(value, it.type.classifier as KClass<*>)
            }
        return ControllerCommand(controller, function, typedArg).execute()
    }
}