package pt.iscte.mei.pa

import pt.iscte.mei.pa.controller.Controller
import pt.iscte.mei.pa.controller.FiboController


/**
 * Main function that initializes the GETJSON application and starts the server.
 * It creates an instance of the GETJSON class with the specified controller classes
 * and starts the server on port 8080.
 */
fun main() {
    val app = GetJson(Controller::class, FiboController::class)
    app.start(8080)
}

