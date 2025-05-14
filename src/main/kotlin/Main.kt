package pt.iscte.mei.pa

import pt.iscte.mei.pa.controller.Controller


fun main() {
    val app = GetJson(Controller::class)
    app.start(8080)
}

