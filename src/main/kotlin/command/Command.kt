package pt.iscte.mei.pa.command

/**
 * Command interface that defines a method to execute a command.
 * The execute method returns an Any type, which can be any object.
 */
interface Command {
    fun execute(): Any
}