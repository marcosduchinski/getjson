package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.http.EndpointRegistry
import java.net.URI

interface DispatchStrategy {
    fun canHandle(uri: URI, registry: EndpointRegistry): Boolean
    fun handle(uri: URI, registry: EndpointRegistry): Any
}