package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import java.net.URI
import kotlin.reflect.KClass

/**
 * Interface for dispatch strategies that handle requests based on the URI and registry.
 *
 */
interface DispatchStrategy {

    /**
     * Determines if the strategy can handle the given URI.
     *
     * @param uri The URI to check.
     * @param registry The endpoint registry to check against.
     * @return True if the strategy can handle the URI, false otherwise.
     */
    fun canHandle(uri: URI, registry: EndpointRegistry): Boolean

    /**
     * Handles the request for the given URI and registry.
     *
     * @param uri The URI to handle.
     * @param registry The endpoint registry to handle against.
     * @return The result of handling the request.
     */
    fun handle(uri: URI, registry: EndpointRegistry): Any


    fun convertToType(value: String, type: KClass<*>): Any = when (type) {
        String::class  -> value
        Int::class     -> value.toInt()
        Long::class    -> value.toLong()
        Double::class  -> value.toDouble()
        Float::class   -> value.toFloat()
        Boolean::class -> value.toBooleanStrictOrNull() ?: error("Invalid boolean: $value")
        else           -> error("Unsupported type: $type")
    }
}