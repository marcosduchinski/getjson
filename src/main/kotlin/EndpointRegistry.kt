package pt.iscte.mei.pa

import annotations.Mapping
import annotations.Param
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

/**
 * EndpointRegistry is a singleton object that manages the registration and retrieval of endpoints.
 * It allows for the registration of classes with annotated methods and provides a way to retrieve
 * the corresponding controller and function based on the request path.
 */
object EndpointRegistry {

    private val registry = mutableMapOf<String, Pair<Any, KFunction<*>>>()

    /**
     * Registers a class with annotated methods as endpoints.
     * It scans the class for methods annotated with @Mapping and registers them in the registry.
     * For each method, searches for the @Param annotation to extract query parameters, if any.
     *
     * @param clazz The class to register.
     */
    fun register(clazz: KClass<*>) {
        val context = clazz.findAnnotation<Mapping>()?.name
        val controller = clazz.createInstance()
        clazz.declaredFunctions.forEach { func ->
            var subPath = func.findAnnotation<Mapping>()?.name
            if (subPath == null) {
                return@forEach
            }
            func.isAccessible = true
            val queryParams = func.parameters
                .filter { it.kind == KParameter.Kind.VALUE }.mapNotNull { param ->
                    val ann = param.findAnnotation<Param>()
                    ann?.let {
                        param.name
                    }
                }
            if (queryParams.isNotEmpty()) {
                val params = queryParams.joinToString(separator = "&") { it + "={${it}}" }
                val fullPath = "/$context/$subPath?$params"
                println(fullPath)
                registry[fullPath] = controller to func
            } else {
                val fullPath = "/$context/$subPath"
                println(fullPath)
                registry[fullPath] = controller to func
            }
        }
    }
    /**
     * Retrieves the registered endpoint that matches the given path.
     * It looks up the registry using the provided path and returns the corresponding controller and function.
     *
     * @param path The path to look up in the registry.
     * @return A pair containing the controller and function if found, null otherwise.
     */
    fun getByPath(path: String): Pair<Any, KFunction<*>>? {
        return registry[path]
    }
    /**
     * Retrieves the first registered endpoint that matches the given regex path.
     * It iterates through the registry and checks if any path contains the regex pattern.
     *
     * @param genericPathRegex The regex pattern to match against the registered paths.
     * @return A pair containing the controller and function if a match is found, null otherwise.
     */
    fun getFirstByRegexPath(genericPathRegex: String): Pair<Any, KFunction<*>>? {
        registry.keys.forEach { path ->
            if (path.contains(Regex(genericPathRegex))) {
                return registry[path]
            }
        }
        return null
    }
}