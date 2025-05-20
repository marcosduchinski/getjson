package pt.iscte.mei.pa

import pt.iscte.mei.pa.strategy.DispatchStrategy
import pt.iscte.mei.pa.strategy.ExactPathStrategy
import pt.iscte.mei.pa.strategy.PathVariableStrategy
import pt.iscte.mei.pa.strategy.QueryParameterStrategy
import java.net.URI

/**
 * Dispatcher class that handles incoming requests based on the URI and the registered endpoints.
 *
 * @property registry The endpoint registry that contains the mapping of paths to controller methods.
 */
class Dispatcher(val registry: EndpointRegistry) {

    /**
     * List of dispatch strategies to handle different types of requests.
     */
    private val strategies: List<DispatchStrategy> = listOf(
        ExactPathStrategy(),
        PathVariableStrategy(),
        QueryParameterStrategy()
    )

    /**
     * Executes the appropriate strategy based on the URI and returns the result.
     *
     * @param uri The URI of the incoming request.
     * @return The result of handling the request, or "404" if no strategy can handle it.
     */
    fun execute(uri: URI): Any {
        for (strategy in strategies) {
            if (strategy.canHandle(uri, registry)) {
                return strategy.handle(uri, registry)
            }
        }
        return "404"
    }

}