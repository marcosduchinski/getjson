package pt.iscte.mei.pa

import pt.iscte.mei.pa.controller.Controller
import pt.iscte.mei.pa.controller.FiboController


fun main() {
    val app = GetJson(Controller::class, FiboController::class)
    app.start(8080)
}

