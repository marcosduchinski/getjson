package pt.iscte.mei.pa

import annotations.Mapping
import annotations.Param
import annotations.Path
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
            if (subPath!!.contains("{")) {
                subPath = subPath.split("/").dropLast(1).joinToString("/")
            }
            var fullPath = "/$context/$subPath"
            val queryParams = func.parameters
                .filter { it.kind == KParameter.Kind.VALUE }.mapNotNull { param ->
                    val ann = param.findAnnotation<Param>()
                    ann?.let {
                        param.name
                    }
                }
            val pathParams = func.parameters
                .filter { it.kind == KParameter.Kind.VALUE }.mapNotNull { param ->
                    val ann = param.findAnnotation<Path>()
                    ann?.let {
                        param.name
                    }
                }
            val params = queryParams.joinToString(separator = "&") { it + "={${it}}" }
            val pathParamsString = pathParams.joinToString(separator = "/") {"{${it}}" }
            if (params.isNotEmpty()) {
                fullPath += "?$params"
            } else if (pathParamsString.isNotEmpty()) {
                fullPath += "/$pathParamsString"
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