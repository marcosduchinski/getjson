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

object EndpointRegistry {

    private val registry = mutableMapOf<String, Pair<Any, KFunction<*>>>()

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

    fun getByPath(path: String): Pair<Any, KFunction<*>>? {
        return registry[path]
    }
    fun getFirstByRegexPath(genericPathRegex: String): Pair<Any, KFunction<*>>? {
        registry.keys.forEach { path ->
            if (path.contains(Regex(genericPathRegex))) {
                return registry[path]
            }
        }
        return null
    }
}