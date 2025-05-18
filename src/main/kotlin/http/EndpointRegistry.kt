package pt.iscte.mei.pa.http

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
        val basePath = clazz.findAnnotation<Mapping>()?.name
        val controller = clazz.createInstance()
        clazz.declaredFunctions.forEach { func ->
            val subPath = func.findAnnotation<Mapping>()?.name
            var fullPath = "/$basePath/$subPath"
            val queryParams = func.parameters
                .filter { it.kind == KParameter.Kind.VALUE }.mapNotNull { param ->
                    val ann = param.findAnnotation<Param>()
                    ann?.let {
                        param.name
                    }
                }
            val params = queryParams.joinToString(separator = "&") { it.toString() + "={${it}}" }
            if (params.isNotEmpty()) {
                fullPath += "?$params"
            }
            println(fullPath)
            func.isAccessible = true
            registry[fullPath] = controller to func
        }
    }

    fun getByPath(path: String): Pair<Any, KFunction<*>>? {
        return registry[path]
    }
}