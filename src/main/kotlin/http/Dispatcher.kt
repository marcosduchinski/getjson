package pt.iscte.mei.pa.http

import pt.iscte.mei.pa.strategy.DispatchStrategy
import pt.iscte.mei.pa.strategy.ExactPathStrategy
import java.net.URI

class Dispatcher(val registry: EndpointRegistry) {

    private val strategies: List<DispatchStrategy> = listOf(
        ExactPathStrategy()
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