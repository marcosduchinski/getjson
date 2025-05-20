package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import pt.iscte.mei.pa.command.ControllerCommand
import java.net.URI
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Strategy for handling requests with path variables in the URI.
 *
 * This strategy checks if the URI has no query parameters and if the path matches a registered endpoint
 * with a path variable. If both conditions are met, it executes the corresponding controller method
 * with the extracted path variable value.
 *
 * @constructor Creates an instance of PathVariableStrategy.
 */
class PathVariableStrategy : DispatchStrategy {

    /**
     * Determines if the strategy can handle the given URI.
     *
     * @param uri The URI to check.
     * @param registry The endpoint registry to check against.
     * @return True if the strategy can handle the URI, false otherwise.
     */
    override fun canHandle(uri: URI, registry: EndpointRegistry): Boolean {
        val parts = uri.path.split("/")
        if (uri.query != null || parts.size < 4) return false
        val regexPath = parts.dropLast(1).joinToString("/") + "/\\{.+\\}"
        return registry.getFirstByRegexPath(regexPath) != null
    }

    /**
     * Handles the request for the given URI and registry.
     *
     * @param uri The URI to handle.
     * @param registry The endpoint registry to handle against.
     * @return The result of handling the request.
     */
    override fun handle(uri: URI, registry: EndpointRegistry): Any {
        val parts = uri.path.split("/")
        val value = parts.last()
        val regexPath = parts.dropLast(1).joinToString("/") + "/\\{.+\\}"
        val (controller, function) = registry.getFirstByRegexPath(regexPath)!!
        val typedArg = function.parameters
            .filter { it.kind == KParameter.Kind.VALUE }
            .map {
                convertToType(value, it.type.classifier as KClass<*>)
            }
        return ControllerCommand(controller, function, typedArg).execute()
    }
}