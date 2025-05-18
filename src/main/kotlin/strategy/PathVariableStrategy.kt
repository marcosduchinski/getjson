package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import pt.iscte.mei.pa.command.ControllerCommand
import java.net.URI

class PathVariableStrategy : DispatchStrategy {

    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean {
        val parts = uri.path.split("/")
        if (parts.size < 2) return false
        val genericPath = parts.dropLast(1).joinToString("/") + "/{pathvar}"
        return registry.getByPath(genericPath) != null
    }

    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val parts = uri.path.split("/")
        val value = parts.last()
        val path = parts.dropLast(1).joinToString("/") + "/{pathvar}"
        val (controller, function) = registry.getByPath(path)!!
        return ControllerCommand(controller, function, listOf(value)).execute()
    }
}