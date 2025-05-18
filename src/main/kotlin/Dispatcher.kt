package pt.iscte.mei.pa

import pt.iscte.mei.pa.strategy.DispatchStrategy
import pt.iscte.mei.pa.strategy.ExactPathStrategy
import pt.iscte.mei.pa.strategy.PathVariableStrategy
import pt.iscte.mei.pa.strategy.QueryParameterStrategy
import java.net.URI

class Dispatcher(val registry: EndpointRegistry) {

    private val strategies: List<DispatchStrategy> = listOf(
        ExactPathStrategy(),
        PathVariableStrategy(),
        QueryParameterStrategy()
    )

    fun execute(uri: URI): Any {
        for (strategy in strategies) {
            if (strategy.canHandle(uri, registry)) {
                return strategy.handle(uri, registry)
            }
        }
        return "404"
    }

}