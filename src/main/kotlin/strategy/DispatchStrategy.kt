package pt.iscte.mei.pa.strategy

import pt.iscte.mei.pa.EndpointRegistry
import java.net.URI
import kotlin.reflect.KClass

interface DispatchStrategy {

    fun canHandle(uri: URI, registry: EndpointRegistry): Boolean
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