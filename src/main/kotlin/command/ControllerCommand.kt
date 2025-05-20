package pt.iscte.mei.pa.command

import kotlin.reflect.KFunction

/**
 * A command that invokes a method on a controller with the specified arguments.
 *
 * @param controller The controller instance on which the method will be invoked.
 * @param function The method to be invoked.
 * @param args The arguments to be passed to the method.
 */
class ControllerCommand(
    val controller: Any,
    val function: KFunction<*>,
    val args: List<Any> = emptyList()
) : Command {
    override fun execute(): Any {
        return function.call(controller, *args.toTypedArray())!!
    }
}
