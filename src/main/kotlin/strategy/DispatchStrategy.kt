package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.http.ClassRegistry
import java.net.URI

interface DispatchStrategy {
    fun canHandle(uri: URI, registry: ClassRegistry): Boolean
    fun hande(uri: URI, registry: ClassRegistry): Any
}