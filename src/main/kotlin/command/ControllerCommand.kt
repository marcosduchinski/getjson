package pt.iscte.mei.pa.command

import kotlin.reflect.KFunction

class ControllerCommand(
    val controller: Any,
    val function: KFunction<*>,
    val args: List<Any> = emptyList()
) : Command {
    override fun execute(): Any {
        return function.call(controller, *args.toTypedArray())!!
    }
}
