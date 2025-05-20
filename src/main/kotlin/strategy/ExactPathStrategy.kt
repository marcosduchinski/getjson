package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.command.ControllerCommand
import pt.iscte.mei.pa.EndpointRegistry
import java.net.URI

/**
 * Strategy for handling exact path requests.
 *
 * This strategy checks if the URI has no query parameters and if the path matches a registered endpoint.
 * If both conditions are met, it executes the corresponding controller method.
 *
 * @constructor Creates an instance of ExactPathStrategy.
 */
class ExactPathStrategy : DispatchStrategy {
    /**
     * Determines if the strategy can handle the given URI.
     *
     * @param uri The URI to check.
     * @param registry The endpoint registry to check against.
     * @return True if the strategy can handle the URI, false otherwise.
     */
    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean = uri.query == null && registry.getByPath(uri.path) != null

    /**
     * Handles the request for the given URI and registry.
     *
     * @param uri The URI to handle.
     * @param registry The endpoint registry to handle against.
     * @return The result of handling the request.
     */
    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val (controller, func) = registry.getByPath(uri.path)!!
        return ControllerCommand(controller, func).execute()
    }
}