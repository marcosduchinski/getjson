package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.command.ControllerCommand
import pt.iscte.mei.pa.EndpointRegistry
import java.net.URI

class ExactPathStrategy : DispatchStrategy {
    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean = registry.getByPath(uri.path) != null

    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val (controller, func) = registry.getByPath(uri.path)!!
        return ControllerCommand(controller, func).execute()
    }
}